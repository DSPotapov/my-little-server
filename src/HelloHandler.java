import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;


public class HelloHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        StringBuilder response = new StringBuilder();
        System.out.println("Началась обработка /hello запроса от клиента.");

        /* полчучаем request body */
        InputStream inputStream = httpExchange.getRequestBody();
        String requestName = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        System.out.println("Тело запроса:\n" + requestName);

        /*  получаем заголовки */
        Headers requestHeaders = httpExchange.getRequestHeaders();
        response.append("Request headers: \n")
                .append(requestHeaders.entrySet())
                .append("\n<------------------->\n");
        System.out.println(response);

        /* получаем метод */
        String method = httpExchange.getRequestMethod();
        System.out.println("Началась обработка " + method + " /hello запроса от клиента.");

        /* получаем URI */
        URI requestURI = httpExchange.getRequestURI();
        String path = requestURI.getPath();
        String name = path.split("/")[2];
        System.out.println("name = " + name);
        

        switch (method) {
            case "POST" -> response.append("Вы использовали метод POST!");
            case "GET" -> response.append("Привет ").append(name)
                    .append("! Рады видеть на нашем сервере.");
            default -> response.append("Вы использовали какой-то другой метод!");
        }

        httpExchange.sendResponseHeaders(200, 0);

        // отправляем тело ответа, записывая строку в выходящий поток
        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(response.toString().getBytes());
        }
    }
}
