# DAL CONNECT
Dal connect is an application that helps international students integrate into the Dalhousie environment as seamlessly and fast as possible. It is designed to connect new international students together and also provide students with valuable information,Dal connect is an essential application because it helps solve some of the challenges international students face ranging from questions about getting directions to budget planning and questions about classes, courses and general lifestyle in Dalhousie. Features implemented on this include, **buildings location** and **direction**, **new students to-do list**, **student chat** and **budget calculation**.


## Libraries
Provide a list of **ALL** the libraries you used for your project. Example:

**google-gson:** Gson is a Java library that can be used to convert Java Objects into their JSON representation. It can also be used to convert a JSON string to an equivalent Java object. Source [here](https://github.com/google/gson)

## Installation Notes
Installation instructions for markers.

## Code Examples
You will encounter roadblocks and problems while developing your project. Share 2-3 'problems' that your team solved while developing your project. Write a few sentences that describe your solution and provide a code snippet/block that shows your solution. Example:

**Problem 1: We needed a method to calculate a Fibonacci sequence**

A short description.
```
// The method we implemented that solved our problem
public static int fibonacci(int fibIndex) {
    if (memoized.containsKey(fibIndex)) {
        return memoized.get(fibIndex);
    } else {
        int answer = fibonacci(fibIndex - 1) + fibonacci(fibIndex - 2);
        memoized.put(fibIndex, answer);
        return answer;
    }
}

// Source: Wikipedia Java [1]
```

**Problem 2: Fetching data from Firebase is in an asynchronous way, which caused a lot of problems, E.g, in same case, only if once all of the data is ready, can we continue to operate the following codes.<br/>**

To wait for all of the data is readly, I was using Handler. When data is ready,  handler will send a message to notify data is ready, and the App could continue the following codes

```
//Sending the message
mHandler.obtainMessage(0).sendToTarget();
```

```
//Handle the message
private Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    userListAdapter = new UserListAdapter(user_list);
                    listView.setAdapter(userListAdapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            UserInformation secondUser = user_list.get(i);
                            FirebaseUser currentUser = AuthUtils.getInstance().getCurrentUser();

                            String room_id = AuthUtils.getInstance().generateRoomId(currentUser.getUid(), secondUser.getUID());
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.fragment_container, ChatFragment.newInstance(secondUser.getUID(), secondUser.getUsername(), secondUser.getUserImage(), room_id), "ChatFragment");
                            fragmentTransaction.commit();

                        }
                    });

                    onReceiveMessages();

                    break;

                default:
                    break;
            }
        }

    };
```

**Problem 3: Regarding the Dalconnect needs to load a number of pictures from the backend, to improve effciency I am using the buffer to store the pictures in memory in case it will be used again in the other activity.<br/>**

```
//Singleton Mode to make sure the buffer only store in one piece.
public class PortraitUtils {

    private static PortraitUtils instance;
    private static LruCache<String, BitmapDrawable> mImageCache;

    private PortraitUtils() {}

    public static PortraitUtils getInstance()
    {
        if(instance==null)
        {
            instance = new PortraitUtils();

            int maxCache = (int) Runtime.getRuntime().maxMemory();
            int cacheSize = maxCache / 8;
            mImageCache = new LruCache<String, BitmapDrawable>(cacheSize) {
                @Override
                protected int sizeOf(String key, BitmapDrawable value) {
                    return value.getBitmap().getByteCount();
                }
            };
        }

        return instance;
    }
}

//When the file is not found in the buffer, load the file remotely

 StorageReference storageRef = storage.getReference().child("Portraits/"+ portraitName);

        try
        {
            final File  localFile = File.createTempFile(portraitName, "jpg");

            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    // Local temp file has been created

                    if (iv != null ) {
                        Bitmap bitmap = getBitmap(localFile);
                        iv.setImageBitmap(bitmap);
                        PortraitUtils.getInstance().setPortraitbyName(portraitName, new BitmapDrawable(bitmap));
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors

                    System.out.println("Handle any errors");
                }
            });


        }
        catch (IOException e) {
            e.printStackTrace();
        }
```


## Feature Section
List all the main features of your application with a brief description of each feature.
- **Building's index**:<br>
The Dalhousie building's index helps to direct students from their present location to building locations they are unfamiliar with.
- **User Chat**:<br>
This feature enables students to communicate effectively with one another.
- **To-do list**:<br>
This is a list of important tasks that new international students need to do at different time intervals I.e. register for courses,renew study visa or buy a new sim card. 
- **Chatbot**:<br>
The Chat bot will help Students handle simple queries e.g. provide student with information about how to get a sim card as well as other informations.

## Final Project Status
Write a description of the final status of the project. Did you achieve all your goals? What would be the next step for this project (if it were to continue)?

#### Minimum Functionality
- Building's index (Completed)
- User registration (Completed)
- To-do list (Completed)

#### Expected Functionality
- User Chat(Texting) (Completed)
- Matching (Find friends by the same country and start term) (Completed)
- New Messages Notification (Completed)

#### Bonus Functionality
- Chatbot(Completed)

## Sources
Use IEEE citation style.
What to include in your project sources:
- Stock images
- Design guides
- Programming tutorials
- Research material
- Android libraries
- Everything listed on the Dalhousie [*Plagiarism and Cheating*](https://www.dal.ca/dept/university_secretariat/academic-integrity/plagiarism-cheating.html)
- Remember AC/DC *Always Cite / Don't Cheat* (see Lecture 0 for more info)

[1] "Java (programming language)", En.wikipedia.org, 2018. [Online]. Available: https://en.wikipedia.org/wiki/Java_(programming_language).
