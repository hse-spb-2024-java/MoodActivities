package org.hse.moodactivities.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.hse.moodactivities.common.proto.requests.defaults.PeriodType;
import org.hse.moodactivities.data.entities.mongodb.MoodFlowRecord;
import org.hse.moodactivities.data.entities.mongodb.User;
import org.hse.moodactivities.data.entities.mongodb.UserDayMeta;
import org.hse.moodactivities.data.promts.PromptsStorage;
import org.hse.moodactivities.services.StatsService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

//@RunWith(MockitoJUnitRunner.class)
public class PromptGeneratorTest {

    @Mock
    private PromptsStorage promptsStorage;

    @Mock
    private StatsService statsService;

    @InjectMocks
    private PromptGenerator promptGenerator;

    private List<UserDayMeta> metas;
    private List<MoodFlowRecord> records;

    @Before
    public void setUp() {
        // Setup common objects for tests
        metas = Arrays.asList(
                new UserDayMeta(/* setup fields */),
                new UserDayMeta(/* setup fields */)
        );
        records = Arrays.asList(
                new MoodFlowRecord(/* setup fields */),
                new MoodFlowRecord(/* setup fields */)
        );
    }

    @Test
    public void testGeneratePrompt_EmptyList() {
        List<UserDayMeta> emptyMetas = Collections.emptyList();
        String result = promptGenerator.generatePrompt(emptyMetas, PromptGenerator.Service.metaCreator, "gptMeta", PeriodType.WEEK);

        assertEquals("", result);
    }

    @Test
    public void testGeneratePromptValidInput() {
        // Mock behavior of dependencies
        when(statsService.getCorrectDaysSublist(anyList(), any(PeriodType.class))).thenReturn(metas);
        when(promptsStorage.getString(anyString())).thenReturn("defaultRequest");

        String result = promptGenerator.generatePrompt(metas, PromptGenerator.Service.metaCreator, "gptMeta", PeriodType.WEEK);

        assertNotNull(result);
        assertTrue(result.contains("defaultRequest"));
    }

    @Test
    public void testUnwrapRecordsEmptyRecords() {
        List<MoodFlowRecord> emptyRecords = Collections.emptyList();

        List<String> result = promptGenerator.unwrapRecords(emptyRecords);

        assertEquals(2, result.size());
        assertEquals("", result.get(0));
        assertEquals("", result.get(1));
    }

    @Test
    public void testAddFeedBack_NullUser() {
        String requestString = "request";
        String result = promptGenerator.addFeedBack(requestString, null);

        assertEquals(requestString, result);
    }

    @Test
    public void testAddFeedBack_PositiveFeedback() {
        User user = new User();
        user.setId("test_id");
        String requestString = "request";

        String result = promptGenerator.addFeedBack(requestString, user);

        assertTrue(result.contains("positive feedback"));
    }

    @Test
    public void testAddFeedBack_NegativeFeedback() {
        User user = new User();
        user.setId("test_id");
        String requestString = "request";

        String result = promptGenerator.addFeedBack(requestString, user);

        assertTrue(result.contains("negative feedback"));
    }
}
