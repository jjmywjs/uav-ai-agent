package com.jjm.uavaiagent.tools;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PDFGenerationToolTest {

    @Test
    public void testGeneratePDF() {
        PDFGenerationTool tool = new PDFGenerationTool();
        String fileName = "货运无人机注意事项.pdf";
        String content = "货运无人机注意事项";
        String result = tool.generatePDF(fileName, content);
        assertNotNull(result);
    }
}
