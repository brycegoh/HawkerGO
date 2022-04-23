package com.example.hawkergo;

import static org.junit.Assert.assertEquals;

import com.example.hawkergo.models.HawkerCentre;
import com.example.hawkergo.models.HawkerStall;
import com.example.hawkergo.models.OpeningHours;
import com.example.hawkergo.models.Review;
import com.example.hawkergo.models.Tags;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="httpo://d.android.com/tools/testing">Testing documentation</a>
 */
public class ModelValidatorTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void modelValidator_CorrectOpeningHoursFields_ReturnsTrue(){
        String days = "Monday to Friday";
        String hours = "8am to 8pm";
        OpeningHours openingHours = new OpeningHours(days, hours);
        assertEquals(openingHours.getDays(), days);
        assertEquals(openingHours.getHours(), hours);
    }

    @Test
    public void modelValidator_CorrectHawkerCentreFields_ReturnsTrue(){
        String address = "55 Somapah Road";
        String name = "John Doe";
        String imageUrl = "test.com";

        List<String> stallsID = new ArrayList<>();
        stallsID.add("stall 1");
        stallsID.add("stall 2");

        String days = "Monday to Friday";
        String hours = "8am to 8pm";
        OpeningHours openingHours = new OpeningHours(days, hours);

        HawkerCentre hawkerCentre = new HawkerCentre(address, name, openingHours, imageUrl, stallsID);
        assertEquals(name, hawkerCentre.getName());
        assertEquals(address, hawkerCentre.getAddress());
        assertEquals(imageUrl, hawkerCentre.getImageUrl());
        assertEquals(openingHours, hawkerCentre.getOpeningHours());
        assertEquals(stallsID, hawkerCentre.getStallsID());
    }

    @Test
    public void modelValidator_CorrectHawkerStallFields_ReturnsTrue(){
        String address = "55 Somapah Road";
        String name = "John Doe";
        String hawkerCentreId = "1000000";
        Integer reviewCount = 10;
        Double totalRating = 4.5;

        List<String> imageUrl = new ArrayList<>();
        imageUrl.add("img1");
        imageUrl.add("img2");

        List<String> popularItems = new ArrayList<>();
        popularItems.add("noodles");
        popularItems.add("rice");

        List<String> tags = new ArrayList<>();
        tags.add("veg");
        tags.add("halal");

        List<String> stallsID = new ArrayList<>();
        stallsID.add("stall 1");
        stallsID.add("stall 2");

        String days = "Monday to Friday";
        String hours = "8am to 8pm";
        OpeningHours openingHours = new OpeningHours(days, hours);

        HawkerStall hawkerStall = new HawkerStall(address, name, openingHours, imageUrl, popularItems, tags, hawkerCentreId, reviewCount, totalRating);

        assertEquals(address, hawkerStall.getAddress());
        assertEquals(name, hawkerStall.getName());
        assertEquals(openingHours, hawkerStall.getOpeningHours());
        assertEquals(imageUrl, hawkerStall.getImageUrls());
        assertEquals(popularItems, hawkerStall.getPopularItems());
        assertEquals(tags, hawkerStall.getTags());
        assertEquals(hawkerCentreId, hawkerStall.getHawkerCentreId());
        assertEquals(reviewCount, hawkerStall.getReviewCount());
        assertEquals(totalRating, hawkerStall.getTotalRating());

        Double averageReview = Math.round((totalRating / reviewCount)*100.0)/100.0;
        assertEquals(averageReview, hawkerStall.getAverageReview());
    }

    @Test
    public void modelValidator_CorrectReviewFieldsOne_ReturnsTrue(){
        String name = "John Doe";
        String comment = "Delicious";
        Double stars = 4.5;
        Date dateReviewed = new Date();
        String hawkerStall = "fish ball noodle shop";
        String imageUrl = "img1";
        String profilePicUrl = "xiao ming";

        Review review = new Review(name, comment, stars, dateReviewed, hawkerStall, imageUrl, profilePicUrl);

        assertEquals(name, review.getName());
        assertEquals(comment, review.getComment());
        assertEquals(stars, review.getStars());
        assertEquals(dateReviewed, review.getDateReviewed());
        assertEquals(hawkerStall, review.getHawkerStall());
        assertEquals(imageUrl, review.getImageUrl());
        assertEquals(profilePicUrl, review.getProfilePicUrl());
    }

    @Test
    public void modelValidator_CorrectReviewFieldsTwo_ReturnsTrue(){
        String name = "John Doe";
        String comment = "Delicious";
        Double stars = 4.5;
        Date dateReviewed = new Date();
        String hawkerStall = "fish ball noodle shop";
        String imageUrl = "img1";
        String profilePicUrl = "xiao ming";

        Review review = new Review(name, comment, stars, dateReviewed, hawkerStall, imageUrl, profilePicUrl);
        review.setStars(3.0);
        assertEquals(3.0, review.getStars(), 0);

        Date date = new Date();
        review.setDateReviewed(date);
        assertEquals(date, review.getDateReviewed());

        String newComment = "yummy";
        review.setComment(newComment);
        assertEquals(newComment, review.getComment());

        String newImageUrl = "img10";
        review.setImageUrl(newImageUrl);
        assertEquals(newImageUrl, review.getImageUrl());

        String newProfilePic = "Ariana grande";
        review.setProfilePicUrl(newProfilePic);
        assertEquals(newProfilePic, review.getProfilePicUrl());
    }

    @Test
    public void modelValidator_CorrectTagFields_ReturnsTrue(){
        List<String> categories = new ArrayList<>();
        categories.add("veg");
        categories.add("halal");

        String[] categoriesArray = new String[2];
        categoriesArray[0] = "veg";
        categoriesArray[1] = "halal";

        Tags tags = new Tags();
        tags.setCategories(categories);

        assertEquals(categories, tags.getCategories());
        assertEquals(categoriesArray, tags.getCategoriesArray());
    }

}



