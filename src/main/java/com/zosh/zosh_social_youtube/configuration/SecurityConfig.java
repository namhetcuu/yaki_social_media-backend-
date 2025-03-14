package com.zosh.zosh_social_youtube.configuration;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import javax.crypto.spec.SecretKeySpec;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

//1 class đc đánh anotation là Configurationt
// thi khi spring chạy sẽ init lên và run các method đc đánh dấu là Bean

//Tệp SecurityConfig.java là một cấu hình bảo mật trong Spring Boot sử dụng Spring Security
// để kiểm soát quyền truy cập API. Nó thiết lập các quy tắc bảo mật, bao gồm:
//Xác thực bằng JWT (JSON Web Token)
//Phân quyền truy cập dựa trên vai trò
//Cấu hình CORS (Cross-Origin Resource Sharing)
//Mã hóa mật khẩu bằng BCrypt
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    //Danh sách các endpoint không yêu cầu xác thực (công khai).
    //Người dùng có thể truy cập các API này mà không cần đăng nhập.
    private final String[] PUBLIC_ENDPOINTS = {"/users",
            "/auth/token", "/auth/introspect"
    };

    @Value("${jwt.signerKey}")
    private String signerKey;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(request ->
                request.requestMatchers(HttpMethod.POST, PUBLIC_ENDPOINTS).permitAll()
                        //.anyRequest() nghĩa là tất cả request còn lại phải bắt buộc xác thuc
                        .anyRequest().authenticated());


        //Dùng JWT để xác thực thay vì session.
        //jwtDecoder() sẽ giải mã JWT.
        //jwtAuthenticationConverter() sẽ chuyển JWT thành quyền hạn của người dùng.
        httpSecurity.oauth2ResourceServer(oauth2 ->
                oauth2.jwt(jwtConfigurer ->
                        jwtConfigurer.decoder(jwtDecoder())
                                .jwtAuthenticationConverter(jwtAuthenticationConverter()))
        );
        //CSRF bị vô hiệu hóa vì API này không cần.
        //CORS được kích hoạt để cho phép các domain khác gọi API.
        httpSecurity.csrf(AbstractHttpConfigurer::disable).cors(cors->cors.configurationSource(corsConfigurationSource()));

        return httpSecurity.build();
    }

    //note giải thích tại sao dùng CROS:
    //Khi bạn có backend chạy ở http://localhost:8081 và frontend chạy ở http://localhost:8080,
    // trình duyệt sẽ chặn request từ frontend đến backend vì chúng khác origin.
    //Để cho phép frontend gọi API của backend mà không bị lỗi, bạn phải bật CORS trên server.
    //Ví dụ lỗi nếu không bật CORS:
    //Access to fetch at 'http://localhost:8081/api/users' from origin 'http://localhost:8080' has been blocked by CORS policy.

    //Nó tạo một cấu hình CORS với các quy tắc sau: ✅ Chỉ cho phép frontend (http://localhost:8080) gọi API.
    //✅ Chấp nhận tất cả các phương thức HTTP (GET, POST, PUT, DELETE, ...).
    //✅ Cho phép gửi cookie và token trong request.
    //✅ Chấp nhận tất cả header từ frontend.
    //✅ Cache cấu hình CORS trong 1 giờ để tối ưu hiệu suất.
    private CorsConfigurationSource corsConfigurationSource() {

        //CorsConfigurationSource là giao diện cung cấp cấu hình CORS dựa trên request.
        //getCorsConfiguration(HttpServletRequest request) sẽ được gọi mỗi khi có request đến backend
        return new CorsConfigurationSource() {
            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {

                //Đây là đối tượng chứa toàn bộ quy tắc CORS
                CorsConfiguration config = new CorsConfiguration();
                //Chỉ cho phép request từ http://localhost:8080.
                //Nếu frontend chạy ở domain khác, cần thay đổi giá trị này.
                config.setAllowedOrigins(Arrays.asList("http://localhost:8080", "http://localhost:5173"));
                //* có nghĩa là tất cả các phương thức HTTP đều được phép, bao gồm:
                //GET
                //POST
                //PUT
                //DELETE
                //PATCH
                //...
                config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")); // ✅ Rõ ràng các phương thức được phép
                //Bật Access-Control-Allow-Credentials, nghĩa là:
                //Trình duyệt có thể gửi cookie, JWT, session token cùng với request.
                //Nếu giá trị này là false, trình duyệt sẽ không gửi token kèm theo.
                config.setAllowCredentials(true);
                //Chấp nhận tất cả các header từ request frontend.
                config.setAllowedHeaders(Collections.singletonList("*"));
                //Cho phép client truy cập các header trong response.
                //Ở đây, header Authorization sẽ được gửi về frontend, cho phép frontend đọc nó.
                config.setExposedHeaders(Arrays.asList("Authorization"));
                //Giá trị 3600L nghĩa là trình duyệt sẽ cache cấu hình CORS trong 1 giờ (3600 giây).
                //Khi trình duyệt gửi request CORS tiếp theo, nó sẽ không cần kiểm tra lại server trong vòng 1 giờ.
                config.setMaxAge(3600L);


                return config;
            }
        };

    }


    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter(){

        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

        return jwtAuthenticationConverter;
    }

    @Bean
    JwtDecoder jwtDecoder(){

        //signerKey là mã bí mật được lấy từ application.properties.
        //JWT được ký bằng thuật toán HS512.
        //Khi người dùng gửi request với token JWT, jwtDecoder() sẽ giải mã và xác thực.
        SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "HS512");
        return NimbusJwtDecoder
                .withSecretKey(secretKeySpec)
                .macAlgorithm(MacAlgorithm.HS512)
                .build();
    }

    //Mã hóa mật khẩu với BCrypt
    @Bean
    PasswordEncoder passwordEncoder(){
        //Dùng BCrypt với độ mạnh 10 để mã hóa mật khẩu.
        //Mật khẩu được mã hóa khi lưu vào database.
        return new BCryptPasswordEncoder(10);
    }

    //Cung cấp AuthenticationManager để xác thực người dùng.
    //Spring Security sử dụng nó để kiểm tra tên đăng nhập & mật khẩu.
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
