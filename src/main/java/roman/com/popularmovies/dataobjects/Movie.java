package roman.com.popularmovies.dataobjects;


public class Movie {

    private String mName;
    private int mImage;

    public Movie(String name, int mImage) {
        this.mName = name;
        this.mImage = mImage;
    }

    public String getName() {
        return mName;
    }

    public int getImage() {
        return mImage;
    }

    public void setImage(int image) {
        this.mImage = image;
    }
}
