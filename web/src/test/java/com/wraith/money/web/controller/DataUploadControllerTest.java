package com.wraith.money.web.controller;

import com.wraith.money.web.ApplicationConfig;
import com.wraith.money.web.service.DataUploadService;
import org.junit.Assert;
import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;

@ContextConfiguration(classes = {ApplicationConfig.class})
@WebAppConfiguration
public class DataUploadControllerTest {

    @InjectMocks
    private DataUploadController dataUploadController;

    @Mock
    private DataUploadService dataUploadService;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(dataUploadController).build();
    }

    //@Test
    public void testPerformDataUpload() throws Exception {
        JobExecution jobExecution = new JobExecution(123L);
        jobExecution.setStatus(BatchStatus.COMPLETED);
        Mockito.when(dataUploadService.performUploadData(Mockito.<MultipartFile>any(), Mockito.anyString())).thenReturn(jobExecution);

        MockMultipartFile file = new MockMultipartFile("test-money-upload.csv", "test-money-upload.csv".getBytes("UTF-8"));

        MockHttpServletResponse response = mockMvc.perform(fileUpload("/$service/performDataUpload").file(file)).andReturn().getResponse();

        Assert.assertEquals(response.getStatus(), HttpStatus.OK.value());
    }
}