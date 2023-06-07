package com.onejo.seosuri.service;


import java.io.*;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BaseFont;
import com.onejo.seosuri.domain.classification.Category;
import com.onejo.seosuri.domain.classification.CategoryRepository;
import com.onejo.seosuri.domain.problem.ProblemRepository;
import com.onejo.seosuri.domain.testpaper.TestPaperRepository;
import com.onejo.seosuri.util.ProbNumComparatorTestPaper;

import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;


import com.onejo.seosuri.domain.problem.Problem;
import com.onejo.seosuri.domain.testpaper.TestPaper;
import com.onejo.seosuri.exception.common.BusinessException;
import com.onejo.seosuri.exception.common.ErrorCode;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TestPaperService {

    @Autowired
    JavaMailSenderImpl mailSender;
    private final String SUBJECT = "띵동-! 서수리가 시험지를 배달했어요\uD83E\uDD89";
    private final String CONTENT = "안녕하세요 고객님!\n아래 첨부된 pdf 파일을 다운로드 해주세요\uD83D\uDE04";
    private final TestPaperRepository testPaperRepository;
    private final ProblemRepository problemRepository;
    private final CategoryRepository categoryRepository;

    public void sendEmail(String email, Long testPaperId){
        // ## 1. 시험지, 해설지 만들기
        // 출력할 문제 ArrayList
        ArrayList<String> problems = new ArrayList<>();
        // 출력할 해설 ArrayList
        ArrayList<String> answers = new ArrayList<>();

        // 시험지 만들 문제 불러오기 + 삭제 되지 않은 문제만 불러오기
        Optional<TestPaper> tmpTestPaper = testPaperRepository.findById(testPaperId);
        List<Problem> probList = problemRepository.findAllByTestPaperAndState(tmpTestPaper.get(), "A");
        if(probList.isEmpty()){
            throw new BusinessException(ErrorCode.NO_EXIST_TEST_PAPER);
        }
        // 카테고리 정보 받아오기
        Long categoryId= tmpTestPaper.get().getCategory().getId();
        Optional<Category> tmpCategory = categoryRepository.findById(categoryId);
        if(tmpCategory.isEmpty()){
            throw new BusinessException(ErrorCode.NO_EXIST_CATEGORY_TITLE);
        }
        String category = tmpCategory.get().getTitle().getCategoryName();


        // 시험지 레벨
        String testPaperLevel = tmpTestPaper.get().getLevel();

        // 문제 번호 정렬
        Collections.sort(probList, new ProbNumComparatorTestPaper());

        // 출력할 문제 문장 및 해설 생성하기
        for(int i=0; i<probList.size(); i++){
            String prob = "";
            String ans = "";
            prob += probList.get(i).getProbNum() + ". " + probList.get(i).getContent();
            ans += probList.get(i).getProbNum() + ". \n" + probList.get(i).getExplanation() + "\n\n 답: " + probList.get(i).getAnswer();

            problems.add(prob);
            answers.add(ans);
        }

        // 시험지 생성
        createPdf(problems, testPaperLevel, category, false);
        // 해설지 생성
        createPdf(answers, testPaperLevel, category, true);

        // ## 2. 이메일 발송
        try{
            // true: 첨부 파일 첨부하겠다
            MimeMessage mail = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mail, true, "UTF-8");

            if(email.equals("") || email.isEmpty()){
                throw new BusinessException(ErrorCode.EMPTY_DATA);
            }

            // email 정보 생성
            mimeMessageHelper.setFrom("seosuri2023@gmail.com");
            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setSubject(SUBJECT);
            mimeMessageHelper.setText(CONTENT, false);

//            ClassPathResource classPathResource = new ClassPathResource("/pdf/TestPaper.pdf");
//            FileSystemResource testPaperFile = new FileSystemResource(classPathResource.getFile());
            InputStream inputStream = new ClassPathResource("/pdf/TestPaper.pdf").getInputStream();
            File testPaperFile = File.createTempFile("TestPaper",".pdf");
            mimeMessageHelper.addAttachment("TestPaper.pdf", testPaperFile);

            ClassPathResource classPathResource2 = new ClassPathResource("/pdf/"+"AnswerSheet.pdf");
            FileSystemResource answerFile = new FileSystemResource(classPathResource2.getFile());
            mimeMessageHelper.addAttachment("AnswerSheet.pdf", answerFile);

            mailSender.send(mail);

        } catch(MessagingException e){
            throw new RuntimeException(e);
        } catch(BusinessException e){
            throw new BusinessException(ErrorCode.FAILED_TO_SEND_EMAIL);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void createPdf(ArrayList<String> content, String level, String category, boolean isAnswer) {
        // 도큐먼트 객체 생성
        Document document = new Document(PageSize.A4);

        ClassPathResource classPathResource = new ClassPathResource("/pdf/"+"AnswerSheet.pdf");
        ClassPathResource classPathResource2 = new ClassPathResource("/pdf/"+"TestPaper.pdf");
//        FileSystemResource testPaperFile = new FileSystemResource(classPathResource.getFile());
//        mimeMessageHelper.addAttachment("TestPaper.pdf", testPaperFile);

        if(isAnswer == true){
            try (FileOutputStream os = new FileOutputStream(classPathResource.getFile())){
                PdfWriter.getInstance(document, os);
                document.open();

                // 테이블 생성
                document.addTitle("서수리 답지");
                document.add(answerTable(level, category));
                document.add(contentTable(content));

                document.close();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        else{
            try (FileOutputStream os = new FileOutputStream(classPathResource2.getFile())){
                PdfWriter.getInstance(document, os);
                document.open();

                // 테이블 생성
                document.addTitle("서수리 시험지");
                document.add(testPaperTable(level, category));
                document.add(contentTable(content));

                document.close();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private PdfPTable testPaperTable(String level, String category) throws DocumentException, IOException {
        // 시험지 헤더
        String[][] titles = new String[][]{
                {"반:", "서수리", "난이도: " + level},
                {"이름:", "유형: " + category, "점수:"}
        };

        // 한글 폰트
        //String fontPath = "C:\\springboot\\seosuri\\src\\main\\java\\com\\onejo\\seosuri\\util\\font\\malgun.ttf";
        ClassPathResource fontClassPathResource = new ClassPathResource("/font/"+"malgun.ttf");
//        FileSystemResource testPaperFile = new FileSystemResource(classPathResource.getFile());
        //BaseFont bf = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        BaseFont bf = BaseFont.createFont(fontClassPathResource.getPath(), BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        Font seosuriFont = new Font(bf, 12);
        Font defaultFont = new Font(bf, 10);


        // 시험지 헤더 테이블 생성 (열: 3개, 폭: 90%)
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(90);

        // 시험지 헤더 테이블 컬럼 폭
        float[] colwidth = new float[]{30f, 40f, 30f};
        table.setWidths(colwidth);

        // 시험지 헤더 생성
        for(String[] row : titles){
            for(String data : row){
                Phrase phrase = new Phrase(data, defaultFont);
                PdfPCell cell = new PdfPCell(phrase);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setPaddingTop(20);
                cell.setPaddingRight(30);
                cell.setPaddingLeft(30);
                cell.setPaddingBottom(20);
                cell.setBorderColor(new BaseColor(0,0,0));
                table.addCell(cell);
            }
            table.completeRow();
        }

        return table;
    }

    private PdfPTable answerTable(String level, String category) throws DocumentException, IOException {
        // 시험지 헤더
        String[][] titles = new String[][]{
                {"반:", "서수리", "난이도: " + level},
                {"이름:", "유형: " + category, "점수:"}
        };


        // 한글 폰트
        //String fontPath = "C:\\springboot\\seosuri\\src\\main\\java\\com\\onejo\\seosuri\\util\\font\\malgun.ttf";
        ClassPathResource fontClassPathResource = new ClassPathResource("/font/"+"malgun.ttf");
//        FileSystemResource testPaperFile = new FileSystemResource(classPathResource.getFile());
        //BaseFont bf = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        BaseFont bf = BaseFont.createFont(fontClassPathResource.getPath(), BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        Font seosuriFont = new Font(bf, 12);
        Font defaultFont = new Font(bf, 10);


        // 시험지 헤더 테이블 생성 (열: 3개, 폭: 90%)
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(90);

        // 시험지 헤더 테이블 컬럼 폭
        float[] colwidth = new float[]{30f, 40f, 30f};
        table.setWidths(colwidth);

        // 시험지 헤더 생성
        for(String[] row : titles){
            for(String data : row){
                Phrase phrase = new Phrase(data, defaultFont);
                PdfPCell cell = new PdfPCell(phrase);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setPaddingTop(20);
                cell.setPaddingRight(30);
                cell.setPaddingLeft(30);
                cell.setPaddingBottom(20);
                cell.setBorderColor(new BaseColor(0,0,0));
                table.addCell(cell);
            }
            table.completeRow();
        }

        return table;
    }

    private PdfPTable contentTable(ArrayList<String> answers) throws DocumentException, IOException {
        // 한글 폰트
        //String fontPath = "C:\\springboot\\seosuri\\src\\main\\java\\com\\onejo\\seosuri\\util\\font\\malgun.ttf";
        ClassPathResource fontClassPathResource = new ClassPathResource("/font/"+"malgun.ttf");
//        FileSystemResource testPaperFile = new FileSystemResource(classPathResource.getFile());
        //BaseFont bf = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        BaseFont bf = BaseFont.createFont(fontClassPathResource.getPath(), BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        Font seosuriFont = new Font(bf, 12);
        Font defaultFont = new Font(bf, 10);


        // 시험지 헤더 테이블 생성 (열: 3개, 폭: 90%)
        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(90);


        // 문제 테이블 생성
        for(int i=0; i<answers.size(); i++){
            PdfPCell cell = new PdfPCell();
            //cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPaddingTop(20);
            cell.setPaddingRight(30);
            cell.setPaddingLeft(30);
            cell.setPaddingBottom(20);
            cell.setPhrase(new Phrase(answers.get(i), defaultFont));	// 셀에 글자 작성.
            table.addCell(cell);
        }
        table.completeRow();

        return table;
    }

}
