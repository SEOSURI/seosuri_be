package com.onejo.seosuri.service;

import java.awt.*;
import java.io.*;
import java.nio.charset.Charset;

import com.google.api.Page;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BaseFont;
import org.springframework.stereotype.Component;

import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.css.StyleAttrCSSResolver;
import com.itextpdf.tool.xml.html.CssAppliersImpl;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;


import com.onejo.seosuri.exception.common.BusinessException;
import com.onejo.seosuri.exception.common.ErrorCode;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class TestPaperService {

    @Autowired
    JavaMailSenderImpl mailSender;
    private final String SUBJECT = "띵동-! 서수리가 시험지를 배달했어요\uD83E\uDD89";
    private final String CONTENT = "안녕하세요 고객님!\n아래 첨부된 pdf 파일을 다운로드 해주세요\uD83D\uDE04";

    public void sendEmail(String email, Long testPaperId){
        // 시험지 pdf 만들기

        // 이메일 발송
        try{
            // true: 첨부 파일 첨부하겠다
            MimeMessage mail = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mail, true, "UTF-8");

            if(email.equals("") || email.isEmpty()){
                throw new BusinessException(ErrorCode.EMPTY_DATA);
            }

            mimeMessageHelper.setFrom("seosuri2023@gmail.com");
            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setSubject(SUBJECT);
            mimeMessageHelper.setText(CONTENT, false);

            FileSystemResource file = new FileSystemResource(new File("C:\\pdftest\\newPDF.pdf"));
            mimeMessageHelper.addAttachment("Test_Paper.pdf", file);

            mailSender.send(mail);

        } catch(MessagingException e){
            throw new RuntimeException(e);
        } catch(BusinessException e){
            throw new BusinessException(ErrorCode.FAILED_TO_SEND_EMAIL);
        }
    }


//    public void createTestPaper() {
//        Document document = new Document();
//
//        try {
//            PdfWriter.getInstance(document, new FileOutputStream("C:\\pdftest\\newPDF.pdf"));
//
//            document.open();
//
//            document.add(generateTable());
//
//            document.close();
//
//        } catch (FileNotFoundException | DocumentException e) {
//            e.printStackTrace();
//        }
//
//    }
    private PdfPTable generateTable(){
    PdfPTable table = new PdfPTable(2);
    PdfPCell cell;

    cell = new PdfPCell(new Phrase("Flight Itinerary"));
    cell.setColspan(2);
    table.addCell(cell);

    cell = new PdfPCell(new Phrase("Flight Details"));
    cell.setColspan(2);
    table.addCell(cell);

    return table;
}

//    public void createTestPaper(){
//        String html = "<html>" +
//                "<head></head>" +
//                "<body>" +
//                "<div>" +
//                    "<div id=\"header\">" +
//                        "<div id=\"header-sub\">" +
//                            "<div id=\"header-text\">" +
//                                "<p id=\"plain-text\">반:</p>" +
//                            "</div>" +
//                            "<div id=\"header-title\">" +
//                                "<p id=\"title\">서 수 리</p>" +
//                            "</div>" +
//                            "<div id=\"header-text\">" +
//                                "<p id=\"plain-text\">난이도:</p>" +
//                            "</div>" +
//                        "</div>" +
//                        "<div id=\"header-sub\">" +
//                            "<div id=\"header-text\">" +
//                                "<p id=\"plain-text\">이름:</p>" +
//                            "</div>" +
//                            "<div id=\"header-category\">" +
//                                "<p id=\"plain-text\">블라블라 유형</p>" +
//                            "</div>" +
//                            "<div id=\"header-text\">" +
//                                "<p id=\"plain-text\">점수:</p>" +
//                            "</div>" +
//                        "</div>" +
//                    "</div>" +
//                    "<div id=\"prob-list\">" +
//                        "<div id=\"list-sub-left\">" +
//                        "</div>" +
//                        "<div id=\"list-sub-right\">" +
//                        "</div>" +
//                    "</div>" +
//                    "<div id=\"footer\">" +
//                        "<div>- 1 -</div>" +
//                    "</div>" +
//                "</div>"
//                +"</body></html>";
////        String html = "<html>" +
////                "<head></head>" +
////                "<body>" +
////                "<div id=\"yaong\">Hello world</div>" +
////                "<div>명월입니다.</div>" +
////                "</body>" +
////                "</html>";
//
//        try (FileOutputStream os = new FileOutputStream("C:\\pdftest\\newPDF.pdf")){
//            // pdf Document 생성
//            Document document = new Document(PageSize.A4, 10,10,10,10);
//            PdfWriter writer = PdfWriter.getInstance(document, os);
//            document.open();
//
//            // css 관련 설정
//            StyleAttrCSSResolver cssResolver = new StyleAttrCSSResolver();
//            try (FileInputStream cssStream = new FileInputStream("C:\\springboot\\seosuri\\src\\main\\java\\com\\onejo\\seosuri\\util\\html\\testpaper.css")) {
//                cssResolver.addCss(XMLWorkerHelper.getCSS(cssStream));
//            }
////            try (FileInputStream cssStream = new FileInputStream("C:\\springboot\\seosuri\\src\\main\\java\\com\\onejo\\seosuri\\util\\html\\test.css")) {
////                cssResolver.addCss(XMLWorkerHelper.getCSS(cssStream));
////            }
//
//            // font 설정
//            XMLWorkerFontProvider font = new XMLWorkerFontProvider(XMLWorkerFontProvider.DONTLOOKFORFONTS);
//            font.register("C:\\springboot\\seosuri\\src\\main\\java\\com\\onejo\\seosuri\\util\\font\\malgun.ttf");
//            CssAppliersImpl cssAppliers = new CssAppliersImpl(font);
//            // font 인스턴스 생성
//            HtmlPipelineContext htmlContext = new HtmlPipelineContext(cssAppliers);
//            htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());
//
//            // pdf pipeline 생성
//            PdfWriterPipeline pdfPipeline = new PdfWriterPipeline(document, writer);
//            // Html pipeline 생성
//            HtmlPipeline htmlPipeline = new HtmlPipeline(htmlContext, pdfPipeline);
//            // css pipline 이랑 모두 합치기
//            CssResolverPipeline cssResolverPipeline = new CssResolverPipeline(cssResolver, htmlPipeline);
//            // work 생성 pipeline 연결
//            XMLWorker worker = new XMLWorker(cssResolverPipeline, true);
//
//            // Html 2 Pdf
//            XMLParser xmlParser = new XMLParser(true, worker, Charset.forName("UTF-8"));
//
//            // 출력
//            try (StringReader strReader = new StringReader(html)){
//                xmlParser.parse(strReader);
//            }
//
//            // Document 객체 닫으면서 리소스 반환
//            document.close();
//
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        } catch (DocumentException e) {
//            throw new RuntimeException(e);
//        }
//
//    }

    public void createTestPaper() {
        Document document = new Document(PageSize.A4);
        try (FileOutputStream os = new FileOutputStream("C:\\pdftest\\newPDF.pdf")){
            PdfWriter.getInstance(document, os);
            document.open();

            // 테이블 생성
            document.addTitle("서수리 시험지");
            document.add(header());

            document.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private PdfPTable header() throws DocumentException, IOException {
        // 시험지 헤더
        String[][] titles = new String[][]{
                {"반:", "서수리", "난이도:"},
                {"이름:", "블라블라 유형짱~", "점수:"}
        };


        // 한글 폰트
        String fontPath = "C:\\springboot\\seosuri\\src\\main\\java\\com\\onejo\\seosuri\\util\\font\\malgun.ttf";
        BaseFont bf = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        Font seosuriFont = new Font(bf, 12);
        Font defaultFont = new Font(bf, 10);


        // 테이블 생성 (열: 3개, 폭: 90%)
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(90);

        // 테이블 컬럼 폭
        float[] colwidth = new float[]{30f, 40f, 30f};
        table.setWidths(colwidth);

        // 헤더 생성
        for(int i = 0; i<20; i++){
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
        }


        return table;
    }




}
