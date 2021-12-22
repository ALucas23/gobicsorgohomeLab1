package news;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import io.reactivex.rxjava3.subjects.Subject;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.concurrent.CompletionStage;

public class NewsFetcher {
    static private final String URL = "127.0.0.1:42070";
    private HttpClient client;
    private Subject<News> newsObservable;
    private DocumentBuilder docParser;
    public NewsFetcher(){
        this.newsObservable = BehaviorSubject.create();
        this.client = HttpClient.newHttpClient();

        try {
            docParser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException("The Parser could not be created");
        }
        WebSocket.Listener wsListener = new WebSocket.Listener() {
            @Override
            public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
                try {
                    Document result = docParser.parse(new ByteArrayInputStream(data.toString().getBytes()));
                    newsObservable.onNext(new News(result.getElementById("agency").getTextContent(),
                            result.getElementById("text").getTextContent()));
                } catch (SAXException | IOException e) {
                    e.printStackTrace();
                }
                return WebSocket.Listener.super.onText(webSocket, data, last);
            }

            @Override
            public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
                return WebSocket.Listener.super.onClose(webSocket, statusCode, reason);
            }
        };
        client.newWebSocketBuilder().buildAsync(URI.create(URL), wsListener);
    }
}
