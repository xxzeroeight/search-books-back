package com.search.books.book.service;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Service
public class OCRService
{
    private final Tesseract tesseract;

    public OCRService(@Value("${ocr.tesseract.data-path:tessdata}") String dataPath,
                      @Value("${ocr.tesseract.language:eng}") String language)
    {
        this.tesseract = new Tesseract();
        this.tesseract.setDatapath(dataPath); // Tesseract가 언어 모델을 찾을 디렉토리 경로
        this.tesseract.setLanguage(language); // 인식할 언어

        // 엔진 모드 설정 (Neural nets LSTM engine only, 가장 정확)
        this.tesseract.setOcrEngineMode(1);

        // 페이지 분할 모드 설정 (Automatic page segmentation with OSD, 방향 감지 포함)
        this.tesseract.setPageSegMode(1);
    }

    /**
     * 이미지 파일에서 텍스트 추출
     */
    public String extractTextFromImage(MultipartFile imageFile)
    {
        if (imageFile == null || imageFile.isEmpty()) {
            throw new IllegalArgumentException("이미지 파일이 없습니다.");
        }

        try {
            BufferedImage image = ImageIO.read(imageFile.getInputStream());
            if (image == null) {
                throw new IllegalArgumentException("유효하지 않은 이미지 파일입니다.");
            }

            BufferedImage processedImage = preprocessImage(image);
            String extractedText = tesseract.doOCR(processedImage);

            return cleanupText(extractedText);
        } catch (TesseractException e) {
            throw new RuntimeException("OCR 처리 중 오류가 발생했습니다: " + e.getMessage(), e);
        } catch (IOException e) {
            throw new RuntimeException("이미지 파일을 읽을 수 없습니다: " + e.getMessage(), e);
        }
    }

    /**
     * 이미지 전처리 (OCR 정확도 향상)
     * 그레이 스케일 변환: OCR 정확도 향상 (색상 정보 제거), 처리 속도 향상, 메모리 사용량 감소
     */
    private BufferedImage preprocessImage(BufferedImage originalImage)
    {
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        BufferedImage grayImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D g2d = grayImage.createGraphics();
        g2d.drawImage(originalImage, 0, 0, null);
        g2d.dispose();

        return grayImage;
    }

    /**
     * 추출된 텍스트 정리
     */
    private String cleanupText(String rawText)
    {
        if (rawText == null) {
            return "";
        }

        return rawText.trim()
                .replaceAll("\\s+", " ")
                .replaceAll("[\\r\\n]+", " ");
    }
}
