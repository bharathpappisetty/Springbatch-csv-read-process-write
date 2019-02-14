package com.example.cdt.config;


import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.MultiResourceItemWriter;
import org.springframework.batch.item.file.ResourceSuffixCreator;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import com.example.cdt.model.Coesui;
import com.example.cdt.process.TransformProcessor;
 
 
@Configuration
@EnableBatchProcessing
public class BatchConfig
{
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
     
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
 
    @Value("${source.folder.location}") //file:/home/input/performance*.csv
    private Resource[] inputResources;
    
    @Value("${destination.folder.location}")
    private String destFolder;
    
 
    @Bean
    public FlatFileItemWriter<Coesui> writer()
    {
        //Create writer instance
        FlatFileItemWriter<Coesui> writer = new FlatFileItemWriter<>();
         
      
//        writer.setResource(outputResource);
         
        //All job repetitions should "append" to same output file
//        writer.setAppendAllowed(true);
        

         
        //Name field values sequence based on object properties
        writer.setLineAggregator(new DelimitedLineAggregator<Coesui>() {
            {
                setDelimiter(",");
                setFieldExtractor(new BeanWrapperFieldExtractor<Coesui>() {
                    {
                        setNames(new String[] { "userName", "userId", "deviceName","dateTime","parameter","metricvalue","location" });
                    }
                });
            }
        });
        return writer;
    }
    
    
    
    @Bean
    public MultiResourceItemWriter<Coesui> multiWriter() {
    	
    	MultiResourceItemWriter<Coesui> writer = new MultiResourceItemWriter<>();
    	writer.setResource(new FileSystemResource(destFolder));
    	writer.setResourceSuffixCreator(new ResourceSuffixCreator() {
			
			@Override
			public String getSuffix(int index) {
				// TODO Auto-generated method stub
				long ts = System.currentTimeMillis();
				return ts+".csv";
			}
		});
    	writer.setItemCountLimitPerResource(10000);
    	writer.setDelegate(writer());
    	return writer;
    	
    }
    
 
    @Bean
    public Job readCSVFilesJob() {
        return jobBuilderFactory
                .get("readCSVFilesJob")
                .incrementer(new RunIdIncrementer())
                .start(step1())
                .build();
    }
 
    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1").<Coesui, Coesui>chunk(500)
                .reader(multiResourceItemReader())
                .processor(processor())
                .writer(multiWriter())
                .build();
    }
    
    @Bean
    public ItemProcessor<Coesui, Coesui> processor() {
        return new TransformProcessor();
    }
 
    @Bean
    public MultiResourceItemReader<Coesui> multiResourceItemReader()
    {
        MultiResourceItemReader<Coesui> resourceItemReader = new MultiResourceItemReader<Coesui>();
        resourceItemReader.setResources(inputResources);
        resourceItemReader.setDelegate(reader());
         return resourceItemReader;
    }
 
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Bean
    public FlatFileItemReader<Coesui> reader()
    {
        //Create reader instance
        FlatFileItemReader<Coesui> reader = new FlatFileItemReader<Coesui>();
         
//        //Set number of lines to skips. Use it if file has header rows.
//        reader.setLinesToSkip(1);  
         
        //Configure how each line will be parsed and mapped to different values
        reader.setLineMapper(new DefaultLineMapper() {
            {
                //3 columns in each row
                setLineTokenizer(new DelimitedLineTokenizer() {
                    {
                        setNames(new String[] { "userName", "userId", "deviceName","dateTime","parameter","metricvalue","location" });
                    }
                });
                //Set values in Coesui class
                setFieldSetMapper(new BeanWrapperFieldSetMapper<Coesui>() {
                    {
                        setTargetType(Coesui.class);
                    }
                });
            }
        });
        return reader;
    }
}