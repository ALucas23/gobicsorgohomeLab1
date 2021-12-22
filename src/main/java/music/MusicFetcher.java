package music;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Thread.sleep;

public class MusicFetcher{
    static final String URL = "http://127.0.0.1:42069";
    static final String URL_RANDOM = "http://127.0.0.1:42069/random";
    static final String URL_GENRE = "http://127.0.0.1:42069/most_popular_genre";
    private final HttpRequest request;
    private final HttpRequest requestGenre;
    private Lock clientLock;
    private final HttpClient client;
    private ExecutorService executor;
    private Scheduler musicSheduler;
    public MusicFetcher(){
        this.musicSheduler = Schedulers.single();
        this.clientLock = new ReentrantLock();
        this.executor = Executors.newFixedThreadPool(1);
        this.client = HttpClient.newHttpClient();
        this.request = HttpRequest.newBuilder().uri(URI.create(URL_RANDOM)).timeout(Duration.ofSeconds(5)).build();
        this.requestGenre = HttpRequest.newBuilder().uri(URI.create(URL_GENRE)).timeout(Duration.ofSeconds(5)).build();
    }

    private void noticeFail(){
        System.out.println("Failed Fetch, Retrying after 3 sec delay.");
        try {
            sleep(3000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    private Music getMusic(){
        try {
            clientLock.lock();
            HttpResponse<String> response = this.client.send(request, HttpResponse.BodyHandlers.ofString());
            return new Music(response.body());
        } catch (IOException | InterruptedException e) {
            noticeFail();
            return getMusic();
        } finally {
            clientLock.unlock();
        }
    }

    private String getGenre(){
        try {
            clientLock.lock();
            HttpResponse<String> response = client.send(requestGenre, HttpResponse.BodyHandlers.ofString());
            return response.body();//TODO add json parser for grade
        } catch (IOException | InterruptedException e) {
            noticeFail();
            return getGenre();
        } finally {
            clientLock.unlock();
        }
    }

    private Music getMusicAccordingToMood(String genre){
        try {
            clientLock.lock();
            HttpResponse<String> response = this.client.send(HttpRequest.newBuilder().uri(URI.create(URL + "/genre/" + genre)).timeout(Duration.ofSeconds(5)).build(), HttpResponse.BodyHandlers.ofString());
            return new Music(response.body(), genre);
        } catch (IOException | InterruptedException e) {
            noticeFail();
            return getMusicAccordingToMood(genre);
        } finally {
            clientLock.unlock();
        }
    }

    public Observable<Music> getMusicObservable(){
        FutureTask<Music> fut = new FutureTask<>(this::getMusic);
        executor.submit(fut);
        return Observable.fromFuture(fut)
                .observeOn(musicSheduler)
                .subscribeOn(Schedulers.single());
    }

    public Observable<String> getGenreObservable(){
        FutureTask<String> fut = new FutureTask<>(this::getGenre);
        executor.submit(fut);
        return Observable.fromFuture(fut)
                .observeOn(musicSheduler)
                .subscribeOn(Schedulers.single());
    }

    public Observable<Music> getMusicAccordingToMoodObservable(){
        FutureTask<String> fut = new FutureTask<>(this::getGenre);
        return getGenreObservable()
                .flatMap(genre -> Observable.fromFuture(execFutureMood(genre)))
                .observeOn(musicSheduler)
                .subscribeOn(Schedulers.single());
    }

    private FutureTask<Music> execFutureMood(String genre){
        FutureTask<Music> fut = new FutureTask<Music>(() -> this.getMusicAccordingToMood(genre));
        executor.submit(fut);
        return fut;
    }
}
