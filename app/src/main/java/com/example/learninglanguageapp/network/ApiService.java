// network/ApiService.java
package com.example.learninglanguageapp.network;

import com.example.learninglanguageapp.models.Course;
import com.example.learninglanguageapp.models.Exercise;
import com.example.learninglanguageapp.models.Lesson;
import com.example.learninglanguageapp.models.Request.LoginRequest;
import com.example.learninglanguageapp.models.Request.OrderInfoMomo;
import com.example.learninglanguageapp.models.Request.PaymentRequest;
import com.example.learninglanguageapp.models.Request.RegisterRequest;
import com.example.learninglanguageapp.models.Request.SocialLoginRequest;
import com.example.learninglanguageapp.models.Response.ApiResponse;
import com.example.learninglanguageapp.models.Response.BalanceResponse;
import com.example.learninglanguageapp.models.Response.LoginResponse;
import com.example.learninglanguageapp.models.Response.PaymentResponse;
import com.example.learninglanguageapp.models.Response.UserResponse;
import com.example.learninglanguageapp.models.UIModel.PackagePayment;
import com.example.learninglanguageapp.models.Unit;
import com.example.learninglanguageapp.models.Word;
import retrofit2.http.GET;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @POST("api/Auth/login")
    Call<ApiResponse<LoginResponse>> login(@Body LoginRequest request);

    @POST("api/Auth/register")
    Call<ApiResponse<LoginResponse>> register(@Body RegisterRequest request);

    @POST("api/Auth/external-login")
    Call<ApiResponse<LoginResponse>> socialLogin(@Body SocialLoginRequest request);

    @GET("api/Profile/myInfo")
    Call<ApiResponse<UserResponse>> getMyProfile(@Header("Authorization") String authHeader);

    @GET("api/Words/user/lesson/{lessonId}")
    Call<ApiResponse<List<Word>>> getWordsByLessonOfUser(
            @Path("lessonId") int lessonId
    );

    @GET("api/Exercises/unit/{unitId}")
    Call<ApiResponse<List<Exercise>>> getExercisesByUnit(
            @Path("unitId") int unitId);

    @POST("api/Payment/create-vnpay")
    Call<PaymentResponse> createVnPay(@Body PaymentRequest model);

    @POST("api/Payment/create-momo")
    Call<PaymentResponse> createMomo(@Body PaymentRequest model);
    @GET("api/Payment/verify")
    Call<ApiResponse<Boolean>> verifyPayment(@Query("transactionId") String transactionId);

    @GET("api/Units")
    Call<ApiResponse<List<Unit>>> getUnits();

    @GET("api/Units/details/my")
    Call<ApiResponse<List<Unit>>> getMyUnitsWithDetails(@Header("Authorization") String authHeader);
    
    @GET("api/Lessons/unit/{unitId}")
    Call<ApiResponse<List<Lesson>>> getLessonsByUnit(
            @Path("unitId") int unitId
    );
    @POST("api/Payment/shop")
    Call<ApiResponse<Boolean>> purchaseWithDiamond(@Body PackagePayment packagePayment);

    @POST("api/Auth/revoke-token")
    Call<ApiResponse<String>> revokeToken(@Body String refreshToken);
    @GET("api/Profile")
    Call<ApiResponse<List<UserResponse>>> getAllUsersRanking(@Header("Authorization") String authHeader);

    @GET("api/Courses/languages/{fromId}/{toId}")
    Call<ApiResponse<List<Course>>> getCourses(@Path("fromId") int fromId , @Path("toId") int toId);
}
