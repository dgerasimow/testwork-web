package servlet;

import com.google.gson.Gson;
import models.ConverterWelcome;
import models.Welcome;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;

@WebServlet(name = "weatherServlet", urlPatterns = "/weather")
public class WeatherServlet extends HttpServlet {
    public static final Logger LOGGER = LoggerFactory.getLogger(WeatherServlet.class);
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendRedirect("weather.html");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://api.openweathermap.org/data/2.5/weather?q=" + req.getParameter("city") + "&appid=0d2466174600c426bea83f7208a2ed81&units=metric"))
                    .GET()
                    .build();
            LOGGER.info(req.getParameter("city"));
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//            LOGGER.info(response.body());

            Welcome responseData = ConverterWelcome.fromJsonString(response.body());

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write(makeJson(responseData));
        } catch (URISyntaxException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private String makeJson(Welcome responseData) {
        HashMap<String, Object> responseDataMap = new HashMap<>();
        responseDataMap.put("city", responseData.getName());

        responseDataMap.put("temperature", responseData.getMain().getTemp());
        responseDataMap.put("weather_main", responseData.getWeather().get(0).getMain());
        responseDataMap.put("weather_desc", responseData.getWeather().get(0).getDescription());
        Gson gson = new Gson();
        return gson.toJson(responseDataMap);
    }
}
