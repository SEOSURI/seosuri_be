package com.onejo.seosuri.service;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.css.CssFile;
import com.itextpdf.tool.xml.css.StyleAttrCSSResolver;
import com.itextpdf.tool.xml.html.CssAppliers;
import com.itextpdf.tool.xml.html.CssAppliersImpl;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;
import com.onejo.seosuri.exception.common.BusinessException;
import com.onejo.seosuri.exception.common.ErrorCode;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;


import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItem;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;

import org.springframework.web.multipart.MultipartFile;

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

import java.io.File;


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

            FileSystemResource file = new FileSystemResource(new File("C:\\Users\\신지수\\Pictures\\Saved Pictures\\image2.jpg"));
            mimeMessageHelper.addAttachment("image2.jpg", file);

            mailSender.send(mail);

        } catch(MessagingException e){
            throw new RuntimeException(e);
        } catch(BusinessException e){
            throw new BusinessException(ErrorCode.FAILED_TO_SEND_EMAIL);
        }
    }

    public MultipartFile createTestPaper() throws IOException, DocumentException{

        String htmlcssPath = "C:\\springboot\\seosuri\\src\\main\\java\\com\\onejo\\seosuri\\util\\html";
        String fontPath = "C:\\springboot\\seosuri\\src\\main\\java\\com\\onejo\\seosuri\\service\\font";
        String path = "C:\\pdftest";
        File saveFolder = new File(path);

        // 저장할 위치의 에러 확인
        if (!saveFolder.exists() || saveFolder.isFile()) {
            saveFolder.mkdirs();
        }

        Document document = new Document();

        String filename = "newPDF.pdf";

        String realName = "C:\\pdftest\\" + "newPDF.pdf";
        File pdfFile = new File(realName);

        if (pdfFile.isFile()) {
            pdfFile.delete();
        }

        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(realName));

        document.open();

        String htmlStr = "<html><body style='font-family: MalgunGothic;'>"+ "<div class=\"paper\"><h2>test 문구 입니다 h2는 적용이 잘 되나 오바</h2><div class=\"header\"><div class=\"header-sub\"><div class=\"header-text\"><p style=\"font-weight:bold;\">반:</p></div><div class=\"header-title\"><p class=\"title\">서 수 리</p></div><div class=\"header-text\"><p style=\"font-weight:bold;\">난이도:</p></div></div><div class=\"header-sub\"><div class=\"header-text\"><p style=\"font-weight:bold;\">이름:</p></div><div class=\"header-category\"><p style=\"font-weight:bold;\">블라블라 유형</p></div><div class=\"header-text\"><p style=\"font-weight:bold;\">점수:</p></div></div></div><div class=\"prob-list\"><div class=\"list-sub\" style=\"border-right:2px solid black\"></div><div class=\"list-sub\"></div></div><div class=\"footer\"><div>- 1 -</div></div></div>"  +"</body></html>";
//        String htmlStr =  "<html><head><style>"
//                + ".paper { width: 793.7px; height: 1122.52px; background-color: white; border-width: 2px; border-style: solid; border-color: black; }"
//                + ".header { width: 710px; height: 110px; margin-left: 41.85px; margin-top: 50px; }"
//                + ".prob-list { display: flex; justify-content: center; width: 710px; height: 890px; margin-left: 41.85px; }"
//                + ".footer { display: flex; justify-content: center; width: 710px; height: 50px; border-top-width: 2px; border-top-color: black; border-top-style: solid; margin-left: 41.85px; padding-top: 10px; }"
//                + ".header-sub { display: flex; width: 710px; height: 55px; border-bottom-width: 2px; border-bottom-color: black; border-bottom-style: solid; }"
//                + ".header-text { width: 188.5px; height: 55px; text-align: left; padding-left: 10px; }"
//                + ".header-title { width: 333px; height: 55px; display: flex; justify-content: center; background-color: #EBEBEB; border-top-width: 2px; border-top-color: black; border-top-style: solid; border-right-width: 2px; border-right-color: black; border-right-style: solid; border-left-width: 2px; border-left-color: black; border-left-style: solid; }"
//                + ".header-category { width: 333px; height: 55px; text-align: center; border-right-width: 2px; border-right-color: black; border-right-style: solid; border-left-width: 2px; border-left-color: black; border-left-style: solid; }"
//                + ".title { width: 200px; height: 36px; font-weight: 1000; font-size: 31px; line-height: 0px; text-align: center; }"
//                + ".list-sub { width: 355px; height: 888px; margin-top: 2px; }"
//                + "</style></head>"
//                + "<html><body style='font-family: MalgunGothic;'>"
//                + "<div class=\"paper\"><div class=\"header\"><div class=\"header-sub\"><div class=\"header-text\"><p style=\"font-weight:bold;\">반:</p></div><div class=\"header-title\"><p class=\"title\">서 수 리</p></div><div class=\"header-text\"><p style=\"font-weight:bold;\">난이도:</p></div></div><div class=\"header-sub\"><div class=\"header-text\"><p style=\"font-weight:bold;\">이름:</p></div><div class=\"header-category\"><p style=\"font-weight:bold;\">블라블라 유형</p></div><div class=\"header-text\"><p style=\"font-weight:bold;\">점수:</p></div></div></div><div class=\"prob-list\"><div class=\"list-sub\" style=\"border-right:2px solid black\"></div><div class=\"list-sub\"></div></div><div class=\"footer\"><div>- 1 -</div></div></div>"
//                +"</body></html>";
        // html 파일 불러 오기
//        BufferedReader reader = new BufferedReader(new FileReader(htmlcssPath + "\\testpaper.html"));
//        String htmlStr = "";
//        String str;
//        while((str = reader.readLine())!=null){
//            System.out.println(str);
//            htmlStr += str.trim();
//        }
//        System.out.println(htmlStr);
//        reader.close();

        XMLWorkerHelper helper = XMLWorkerHelper.getInstance();

        CSSResolver cssResolver = new StyleAttrCSSResolver();
        CssFile cssFile = XMLWorkerHelper.getCSS(new FileInputStream(htmlcssPath + "\\testpaper.css"));
        cssResolver.addCss(cssFile);

        XMLWorkerFontProvider fontProvider = new XMLWorkerFontProvider(XMLWorkerFontProvider.DONTLOOKFORFONTS);

        //매우매우 중요!!
        //반드시 해당 폰트가 경로내에 있어야한다.
        //(폰트 존재하지 않을 시 pdf 생성 후 열리지 않는 에러 발생)
        fontProvider.register(fontPath +"\\malgun.ttf", "MalgunGothic"); //MalgunGothic은 font-family용 alias
//        fontProvider.register("src/main/java/com/onejo/seosuri/util/font/malgun.ttf", "MalgunGothic"); //MalgunGothic은 font-family용 alias
        CssAppliers cssAppliers = new CssAppliersImpl(fontProvider);

        HtmlPipelineContext htmlContext = new HtmlPipelineContext(cssAppliers);
        htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());

        // html을 pdf로 변환시작
        PdfWriterPipeline pdf = new PdfWriterPipeline(document, writer);
        HtmlPipeline html = new HtmlPipeline(htmlContext, pdf);
        CssResolverPipeline css = new CssResolverPipeline(cssResolver, html);

        XMLWorker worker = new XMLWorker(css, true);
        //캐릭터 셋 설정
        XMLParser xmlParser = new XMLParser(worker, Charset.forName("UTF-8"));

        StringReader strReader = new StringReader(htmlStr);
        xmlParser.parse(strReader);

        document.close();
        writer.close();

        FileItem fileItem = new DiskFileItem("mainFile", Files.probeContentType(pdfFile.toPath()), false, pdfFile.getName(), (int) pdfFile.length(), pdfFile.getParentFile());

        try {
            InputStream input = new FileInputStream(pdfFile);
            OutputStream os = fileItem.getOutputStream();
            IOUtils.copy(input, os);
        } catch (IOException ex) {
            throw new BusinessException(ErrorCode.INVALID_INPUT);
        }

        MultipartFile multipartFile = new CommonsMultipartFile();

        return multipartFile;
    }
}
