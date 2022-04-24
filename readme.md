# HawkerGO (Team 5B)
![HawkerGo](./readme_resources/logo.jpg "HawkerGO")

# ⚠️ NOTE ⚠️
<b>This project requires a firebase google-services.json file to run. Please contact anyone in the group for the credentials file.</b>

# Developers

Goh Ying Ming, Bryce &nbsp;&nbsp; @brycegoh
<br/>
Sean Chen Zhi En &nbsp;&nbsp; @seancze
<br/>
Umang Sanjeev Gupta &nbsp;&nbsp; @Usgupta
<br/>
Tee Zhi Zhang &nbsp;&nbsp; @ZhiZhangT
<br/>
Lim Shu Hui Pamela &nbsp;&nbsp; @pamz23
<br/>
Sim Shang Hong &nbsp;&nbsp; @shanghongsim
<br/>

# Introduction
HawkerGo aims to be a single, centralised resource for users to view and review hawker stalls and hawkers centres in Singapore. We organise information on hawker centre stalls and present them in an easy to access manner (e.g. opening hours, stall number etc). We also help people locate suitable food stalls in each hawker centre and  populate our app with real and updated information from their own experiences. Ultimately, we also wish to build a vibrant community of hawker lovers on our platform, and keep the hawker culture in Singapore alive!

# Screenshots and user flow
![HawkerGo](./readme_resources/poster.png "HawkerGO")
# Video

[![IMAGE ALT TEXT](http://img.youtube.com/vi/dtdSYYcxvCk/0.jpg)](https://www.youtube.com/watch?v=dtdSYYcxvCk "HawkerGo Demo")


# Folder Structure
Our app is split into different folders, each comprising of files that handle a specific part of our app. The folders are split as such:
```
Folders:

app/src/main/java/com/example/hawkergo
|
└─── activities             
│   └─── baseActivities
│   └─── helpers
│ 
└─── adapters
│ 
└─── fragments
│ 
└─── models
│ 
└─── services
│   └─── interfaces
│   └─── utils
│
└─── utils
│
└─── res
│   └─── color
│   └─── layout
│   └─── menu
│   └─── values
│
```

# Guidelines to contribute or modify code
#### Please stick to the folder structure where each folder will contain code responsible for different aspects of the app.
```
Folders:

app/src/main/java/com/example/hawkergo

|
└─── activities             # All activties goes here    
│   └─── baseActivities     # Activities to extend from these classes. 
│                               # Contains common logic Eg. screens that requires checking of authenticated user 
│                            
│   └─── helpers            # Helper classes or classes with abstracted logic used in activies.
│ 
└─── adapters               # All customer adapters goes here
│ 
└─── fragments              # All fragments goes here
│  
└─── models                 # All data models goes here
│ 
└─── services               # All Database interactions related code goes here
│   └─── interfaces         # Contains interfaces used in Services class
│   └─── utils              # General utility/helper classes
│
└─── utils                  # App-wide helper classes goes here
│
└─── res                    # App-wide resources goes here
│   └─── color              # Colors based on widget states
│   └─── layout             # Layouts used in actitivies/Fragments
│   └─── menu               # Menu and Action bar / toolbar layouts
│   └─── values             # Globally defined colors, styles and themes
│
```

## Guidelines to edit Services
1. If you have a new collection, define them in `utils/FirebaseConstants`
```java
public class FirebaseConstants
{
    public static class CollectionIds {
        public static String HAWKER_CENTRES = "hawkerCentres";
        public static String HAWKER_STALLS = "hawkerStalls";
        public static String TAGS = "tags";
        public static String REVIEWS = "reviews";
        /**
         *  DEFINE NEW COLLECTION NAME HERE
         * 
        */
    }
    // ...
}
```
2. Using an interface in `services/interface/` named `_____Queryable`, list down what CRUD methods you require.
```java
public interface HawkerCentreQueryable {
    static void addHawkerCentre(HawkerCentre hawkerCentre, DbEventHandler<String> eventHandler {};
    static void deleteHawkerCentre(String hawkerCentreID,DbEventHandler<String> callBack){};
    static void getHawkerCentreByID(String hawkerCentreID, DbEventHandler<HawkerCentre> eventHandler){};
    static void addStallIntoHawkerCentre(String hawkerCentreID, HawkerStall newHawkerStall, DbEventHandler<String> eventHandler){};
    static void getAllHawkerCentres(DbEventHandler<List<HawkerCentre>> eventHandler){};
}
```
3. Define your Services class as shown below. 
   > Things to take note:
   > 1. Implement your previously defined Queryable interface.
   > 2. Define the Collection name and reference
   > 3. Document what your method is suppose to do
   > 4. Always accept a `DbEventHandler` as a param. This is used by the activities to define callback functions
   > 5. `DbEventHandler` expects a Generic type which is what you expect the DB to return
   > 6. Always call the `.onSuccess` and pass in the deserialised data using firebase's `.toObject` or `.toObjects` for a list of data
```java
public class HawkerStallsService implements HawkerStallQueryable {
    private static final String collectionId = FirebaseConstants.CollectionIds.HAWKER_STALLS; 
    private static final CollectionReference collectionRef = FirebaseConstants.getCollectionReference(collectionId);

    /**
     * Get a hawker stall by its ID
     * @param hawkerStallID ID of the hawker stall document
     * @param eventHandler  Callback to handle on success or failure events
     */
    public static void getHawkerStallByID(String hawkerStallID, DbEventHandler<HawkerStall> eventHandler){
        DocumentReference docRef = collectionRef.document(hawkerStallID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        HawkerStall hawkerStall = document.toObject(HawkerStall.class);
                        eventHandler.onSuccess(hawkerStall);
                    } else {
                        eventHandler.onSuccess(null);
                    }
                } else {
                    eventHandler.onFailure(task.getException());
                }
            }
        });
    };
    // ...
}
```

## Guidelines to edit Activities
1. Always use the globally defined colors and styles from `res/values/color.xml` and `res/values/styles.xml` respectively. Do add new styles into those folders as well.
2. Our team follows a standard where procedures that happen `onCreate` are abstracted out into meaningful methods. This allows team members to easily understand what the code is for. 

    For example:
```java
 @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hawker_stall);
        newCategories = new ArrayList<>();
        this.initViews(); // find all views by id
        this.handleIntent(); // get extra data from intent
        this.inflateOpeningDaysChips(); // inflate dynamically generated views
        this.getAllTagsAndInflateChips(); // inflate dynamically generated views
        this.initDynamicEditTextManager(); // init abstracted logic
        this.attachButtonEventListeners(); // set button listeners
        this.addFragmentBundleListener(); // listen for fragment bundle
    }
```
3. Any DB queries/writes are done via a Service and require you to define a `DbEventHandler`. For example:
```java
TagsService.getAllTags(new DbEventHandler<Tags>() {
    @Override
    public void onSuccess(Tags o) {
        String[] categoriesArray = o.getCategoriesArray();
        new FilterDialogFragment(categoriesArray).show(fm, FilterDialogFragment.TAG);
    }

    @Override
    public void onFailure(Exception e) {
        Toast.makeText(HawkerStallActivity.this,
                "Unable to retrieve filter tags. Please try again.",
                Toast.LENGTH_SHORT).show();
    }
});
```
4. Any DB interaction that happens on a button click or any user input, requires a debouncer. We have defined it in Utils so just use it like this:
```java
final Debouncer debouncer = new Debouncer();
debouncer.debounce(
        view,
        new Runnable() {
            @Override
            public void run() {
                // put what you want to write to DB here
            }
        }
);
```
5. Please also define `onActivityResult` if your activity consumes anything from Intents. Please also define your Intent data keys, result codes, etc in `/utils/constants`
```java
@Override
public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == Constants.RequestCodes.HAWKER_STALL_LISTING_TO_ADD_STALL_FORM &&
            resultCode == Constants.ResultCodes.TO_HAWKER_STALL_LISTING) {
        hawkerCentreName = data.getStringExtra(Constants.IntentExtraDataKeys.HAWKER_CENTRE_NAME);
        hawkerCentreId = data.getStringExtra(Constants.IntentExtraDataKeys.HAWKER_CENTRE_ID);
    }
}
```
6. For any images that require downloading,please use `DownloadImageTask`
7. If your activity requires the Authentication with a toolbar, add `extends AuthenticatedActivity`. If you just require the toolbar, use `extends ToolbarActivity`.

## Guidelines to edit Models
1. Always extend `BaseDbFields` as it contains document id 
2. Always make the attributes private and selectively include setters to data that are mutable
3. If you have business logic, it can be plaed in models as well