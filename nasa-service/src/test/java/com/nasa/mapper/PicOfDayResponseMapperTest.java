package com.nasa.mapper;

import com.nasa.entity.PicOfDayEntity;
import com.nasa.bean.PicOfDayResponseBean;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PicOfDayResponseMapperTest {

    private PicOfDayResposeMapper cut  = new PicOfDayResposeMapper();
   

    @Test
    public void testMap_SuccessfulMapping() {
        // Arrange: Create a PicOfDayEntity with sample data
        PicOfDayEntity entity = new PicOfDayEntity();
        entity.setCopyright("copyright");
        entity.setExplanation("This is an explanation");
        entity.setUrl("https://example.com");

        // Act: Use the mapper to convert the entity to a response bean
        PicOfDayResponseBean responseBean = cut.map(entity);

        // Assert: Check if the mapping is correct
        assertNotNull(responseBean, "Response bean should not be null");
        assertEquals("copyright", responseBean.getCopyright(), "Copyright should match");
        assertEquals("This is an explanation", responseBean.getExplanation(), "Explanation should match");
        assertEquals("https://example.com", responseBean.getUrl(), "URL should match");
    }

    @Test
    public void testMap_NullValues() {
        // Arrange: Create a PicOfDayEntity with null values
        PicOfDayEntity entity = new PicOfDayEntity();
        entity.setCopyright(null);
        entity.setExplanation(null);
        entity.setUrl(null);

        // Act: Use the mapper to convert the entity to a response bean
        PicOfDayResponseBean responseBean = cut.map(entity);

        // Assert: Check if the mapping handles null values correctly
        assertNotNull(responseBean, "Response bean should not be null");
        assertNull(responseBean.getCopyright(), "Copyright should be null");
        assertNull(responseBean.getExplanation(), "Explanation should be null");
        assertNull(responseBean.getUrl(), "URL should be null");
    }

    @Test
    public void testMap_EmptyEntity() {
        // Arrange: Create a PicOfDayEntity with empty values
        PicOfDayEntity entity = new PicOfDayEntity();
        entity.setCopyright("");
        entity.setExplanation("");
        entity.setUrl("");

        // Act: Use the mapper to convert the entity to a response bean
        PicOfDayResponseBean responseBean = cut.map(entity);

        // Assert: Check if the mapping handles empty strings correctly
        assertNotNull(responseBean, "Response bean should not be null");
        assertEquals("", responseBean.getCopyright(), "Copyright should be an empty string");
        assertEquals("", responseBean.getExplanation(), "Explanation should be an empty string");
        assertEquals("", responseBean.getUrl(), "URL should be an empty string");
    }
}
