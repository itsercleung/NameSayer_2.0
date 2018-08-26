package namesayer;

/**
 * ImageList: Provides functionality of getting the thumbnail image and its labeled creation name. Mainly used for viewLists.
 * AUTHOR: Eric Leung
 */
public class ImageList {

    private String image;
    private String flag;

    public ImageList(String image, String flag) {
        this.image = image;
        this.flag = flag;
    }

    public String getImage() {
        return this.image;
    }

    public String getFlag() {
        return this.flag;
    }

    public String toString() {
        return this.flag;
    }
}