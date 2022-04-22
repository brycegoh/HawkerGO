package com.example.hawkergo;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import com.example.hawkergo.models.HawkerCentre;
import com.example.hawkergo.models.HawkerStall;
import com.example.hawkergo.models.OpeningHours;
import com.example.hawkergo.services.HawkerCentresService;
import com.example.hawkergo.services.interfaces.DbEventHandler;
import com.example.hawkergo.utils.TextValidatorHelper;
import com.google.firebase.FirebaseException;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }
}