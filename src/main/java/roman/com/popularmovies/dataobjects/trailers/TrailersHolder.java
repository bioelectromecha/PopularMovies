
package roman.com.popularmovies.dataobjects.trailers;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TrailersHolder {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("results")
    @Expose
    private List<Trailer> mTrailers = null;

    public TrailersHolder() {
    }

    public Integer getId() {
        return id;

    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Trailer> getTrailers() {
        return mTrailers;
    }

    public void setTrailers(List<Trailer> trailers) {
        this.mTrailers = trailers;
    }

}
