package music;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Music {
    private static Lock playerLock = new ReentrantLock();
    String url, genre;
    public Music(String url){
        this(url, "Unkown");
    }
    public Music(String url, String genre){
        this.url = url;
        this.genre = genre;
    }

    public String getGenre(){
        return genre;
    }

    public void play(){
        System.out.println(url);
    }
}
