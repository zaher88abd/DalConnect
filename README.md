# DAL CONNECT
Dal connect is an application that helps international students integrate into the Dalhousie environment as seamlessly and fast as possible. It is designed to connect new international students together and also provide students with valuable information,Dal connect is an essential application because it helps solve some of the challenges international students face ranging from questions about getting directions to budget planning and questions about classes, courses and general lifestyle in Dalhousie. Features implemented on this include, **buildings location** and **direction**, **new students to-do list**, **student chat** and **budget calculation**.


## Libraries
Packaages:
com.github.d-max:spots-dialog:0.7@aar: for spots dialog (Android alert dialog with moving dots ), can be found on GitHub
com.rengwuxian.materialedittext:library:2.1.4: for material edittext, adding more of the material design guide to androis default Edittext, can be found on Github


## Installation Notes
[1]Download and unzip the **Dalconnect** application folder<br>
[2]Click on the file menu and select open project.<br>
[3]Select the **Dalconnect** module in the Project window and then Click **Run** in the toolbar.<br>
[4]In the Select Deployment Target window, select your device, and click OK.<br>
[5]Android Studio will install Dalconnect on your connected device.<br>

## Code Examples
**Problem 1: RestAPI not available for direction and room**

we had problems with our RestAPI with regards user request for direction and room,this code segment was used to solve this issue
```
for (AIOutputContext context : contextList) {
//                    Log.i(LOG_TAG, "context name: " + context.getName());
                    contextNames.add(context.getName());
                }
//                Log.i(LOG_TAG, "Resolved query: " + result.getResolvedQuery());
//                Log.i(LOG_TAG, "Action: " + result.getAction());

                final String speech = result.getFulfillment().getSpeech();
//                Log.i(LOG_TAG, "Speech: " + speech);

                if (!TextUtils.isEmpty(speech))
                    addMessage(speech, false);

                if (!contextNames.isEmpty() &&
                        contextNames.contains("going") && contextNames.contains("car") && newTrestionRequest) {
                    findFastRout();
                    newTrestionRequest = false;
                } else if (!contextNames.isEmpty() &&
                        contextNames.contains("library") && contextNames.contains("study_room") && newLibraryBook) {
                    openLink();
                    newLibraryBook = false;
                } else {
                    newLibraryBook = true;
                    newTrestionRequest = true;
                }
```

**Problem 2: Fetching data from Firebase is in an asynchronous way, which caused a lot of problems, E.g, in same case, only if all of the data is ready, can we continue to operate the following codes.<br/>**

To wait for all of the data to be ready, a handler was used. When data was ready, the handler sends a message to notify that the data is ready, and the App could continue by the following codes

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


## Feature Section
- **Building's index**:<br>
The Dalhousie building's index helps to direct students from their present location to building locations they are unfamiliar with.
- **User Chat**:<br>
This feature enables students to communicate effectively with one another.
- **To-do list**:<br>
This is a list of important tasks that new international students need to do at different time intervals I.e. register for courses,renew study visa or buy a new sim card. 
- **Chatbot**:<br>
The Chat bot will help Students handle simple queries e.g. provide student with information about how to get a sim card as well as other informations.

## Final Project Status
Overall the project was a success, The **minimum**, **expected** and **bonus** functionalities were all implemented, and our team was able to achieve all the goals that were set for this project. In future works with regards to this application, we would like to add more functionality as well as fine tune the overall design of the application. Also if given permission we would want this application to be accessible to new students of Dalhousie as we believe that **Dalconnect** is a vital application that will help new students integrate into the Dalhousie environment.

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
- [1]Android-Firebase-Chat.[On-line]. Availiable: https://github.com/hieuapp/android-firebase-chat [2018]
- [2]Three-level cache strategy for images in Android  [On-line]. Availiable: https://blog.csdn.net/singwhatiwanna/article/details/9054001[2018]
- [3]android bitmap compress-Image Compression  [On-line]. Availiable: https://blog.csdn.net/luhuajcdd/article/details/8948261[2018]
- [4]https://stackoverflow.com/questions/20438627/getlastknownlocation-returns-null
- [5]https://www.youtube.com/watch?v=QNb_3QKSmMk   
- [6]https://stackoverflow.com/questions/25190886/android-open-map-intent-with-directions-with-two-points
