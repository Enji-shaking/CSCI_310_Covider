package com.example.covider.database.questionnaire;

import android.content.Context;

import static org.junit.Assert.*;

import androidx.test.platform.app.InstrumentationRegistry;

import com.example.covider.config.Config;
import com.example.covider.database.ManagerFactory;
import com.example.covider.model.questionnaire.Questionnaire;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class QuestionnaireManagerTest {
    QuestionnaireManager questionnaireManager;

    @Before
    public void setup(){
        Context instrumentationContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Config.Change_Test();
//        instrumentationContext.deleteDatabase(Config.DATABASE_NAME);
        ManagerFactory.initialize(instrumentationContext);
        questionnaireManager = ManagerFactory.getQuestionnaireManagerInstance();
    }

    @Test
    public void testAddQuestionnaire(){
        Questionnaire q = new Questionnaire(1, 1, 1, false, false, false, false);
        Questionnaire q2 = new Questionnaire(2, 2, 1, true, false, false, false);
        long id = questionnaireManager.addOrUpdateQuestionnaire(q);
        long id2 = questionnaireManager.addOrUpdateQuestionnaire(q2);
        ArrayList<Questionnaire> l = questionnaireManager.getQuestionnaireByBuildingId(1);
        assertEquals(2, l.size());
        assertEquals(q, l.get(0));
        assertEquals(q2, l.get(1));
        questionnaireManager.deleteQuestionnaire(id);
        questionnaireManager.deleteQuestionnaire(id2);
    }
}
