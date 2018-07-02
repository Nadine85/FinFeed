package com.example.android.finfeeds;

/**The class FinFeed refers to an information related to politics or financial topics. a FinFeed object includes its section;
  reference-type; time when the information was published; and its url   */

public class FinFeed {

    //Section i.e Finance/ Politics
    private String mSection;
    //Type i.e. blog or article indicating the significance/ information content
    private String mReferenceType;
    //rating from 1= low t 5 = high indicating the satisfaction of other users
    private int mStarRating;
    //Headline
    private String mWebTitle;
    //time, when the information was published indicating its topicality/ up-to-dateness
    private String mUseDate;
    //url, with which the user can access the FinFeed
    private String mWebUrl;


    /**constructs a new FinFeed object
     * @paramsection = section of the FinFeed object
     * @paramreferenceType = type of the FinFeed object
     * @paramstarRating = rating of the FinFeed object ranging from 1= low to 5 = high
     * @paramwebTitle = headline of the FinFeed object
     * @paramUseDate = time when the FinFeed was published
     * @paramwebUrl = url in order to access the FinFeed object*/

    public FinFeed (String section, String referenceType, int starRating, String webTitle, String useDate, String webUrl) {
        mSection = section;
        mReferenceType = referenceType;
        mStarRating = starRating;
        mWebTitle = webTitle;
        mUseDate = useDate;
        mWebUrl = webUrl;
    }

    //returns the section of the FinFeed
    public String getSection() {
        return mSection;
    }
    //returns the type of the FinFeed
    public String getType() {
        return mReferenceType;
    }
    //returns the starRating of the FinFeed
    public int getStarRating() {
        return mStarRating;
    }
    //returns the headline of the FinFeed
    public String getWebTitle() {
        return mWebTitle;
    }
    //returns the time of the FinFeed
    public String getUseDate() {
        return mUseDate;
    }

    //returns the urls of the FinFeed
    public String getWebUrl(){
        return mWebUrl;
    }
}
