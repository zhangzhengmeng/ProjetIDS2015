import muthesius.net.*;
import org.webbitserver.*;
import SimpleOpenNI.*;
import processing.opengl.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
WebSocketP5 ws;
SimpleOpenNI context;

boolean      handsTrackFlag = false;
PVector      handVec = new PVector();
ArrayList    handVecList = new ArrayList();
int          handVecListSize = 30;
//String       lastGesture = "";
float        posx, posy, x, y;
float        easing = 0.15; //ease value. 0.001 = very smooth

XnVSessionManager sessionManager;
XnVFlowRouter flowRouter;
//XnVPushDetector pushDetector;
XnVSwipeDetector swipeDetector;

Server server;
PointDrawer pointDrawer;

void setup() {
  frameRate(24);
  context = new SimpleOpenNI(this);
  context.setMirror(true);
  if (context.enableDepth() == false) {
    println("Can't open the depthMap, maybe the camera is not connected!"); 
    exit();
    return;
  }
  context.enableGesture();
  context.enableHands();
  context.enableDepth();

  sessionManager = context.createSessionManager("Click,Wave", "RaiseHand");

  //new Push(sessionManager, server); //activate PUSH detection
  new Swipe(sessionManager, server); //activate SWIPE detection

  pointDrawer = new PointDrawer();
  flowRouter = new XnVFlowRouter();
  //pushDetector = new XnVPushDetector();
  swipeDetector = new XnVSwipeDetector();
  flowRouter.SetActive(pointDrawer);

  sessionManager.AddListener(flowRouter);

  size(context.depthWidth(), context.depthHeight()); 
  println(context.depthWidth()+" - "+context.depthHeight());
  ws = new WebSocketP5(this, 8080);
}

void draw() {
  background(200, 0, 0);
  context.update();
  context.update(sessionManager);
  
  //display kinect vision
  image(context.depthImage(), 0, 0);
  
  //visual hand tracking
  pointDrawer.draw();
}

// session callbacks

void onStartSession(PVector pos) {
  println("onStartSession: " + pos);
  ws.broadcast("START start");
}
void onEndSession() {
  println("onEndSession: ");
  ws.broadcast("END end");
}
void onFocusSession(String strFocus, PVector pos, float progress) {
  println("onFocusSession: focus=" + strFocus + ",pos=" + pos + ",progress=" + progress);
}
void onCreateHands(int handId, PVector pos, float time) {
  //println("onCreateHands - handId: " + handId + ", pos: " + pos + ", time:" + time);
  handsTrackFlag = true;
  handVec = pos;
  handVecList.clear();
  handVecList.add(pos);
}
void onUpdateHands(int handId, PVector pos, float time) {
  //println("onUpdateHandsCb - handId: " + handId + ", pos: " + pos + ", time: " + time);
  handVec = pos;
  
  //x & y with top / left origin
  posx = ceil(pos.x + (context.depthWidth()-40)/2); // + position x - half of the with minus the "dead zone" around the scene
  posy = -ceil(pos.y - (context.depthHeight()+0)/2); // - position Y - half of the height minus the "dead zone" around the scene
  
  //avoid screen overflow
  if(posx < 0) posx = 0;
  if(posx > 620) posx = 620;
  if(posy < 0) posy = 0;
  if(posy > 240) posy = 240;
    
  //easing for smooth movements
  float dx = posx - x;
  x += dx * easing;
  float dy = posy - y;
  y += dy * easing;
  //println("MOVE x:"+x+" | y: "+y+" | z: "+pos.z+" | time: "+time);
  handVecList.add(0, x+" "+y+" "+pos.z+" "+time);
  
  //send position x,y,z & time to ws & arrayList
  ws.broadcast("MOVE "+x+" "+y+" "+pos.z+" "+time);                                                                              
  if (handVecList.size() >= handVecListSize) {
    handVecList.remove(handVecList.size()-1);
  }
}

// PointDrawer keeps track of the handpoints

class PointDrawer extends XnVPointControl {
  HashMap    _pointLists;
  int        _maxPoints;
  color[]    _colorList = { color(255, 0, 0), color(0, 255, 0), color(0, 0, 255), color(255, 255, 0)};

  public PointDrawer() {
    _maxPoints = 300;
    _pointLists = new HashMap();
  }

  public void OnPointCreate(XnVHandPointContext cxt) {
    // create a new list
    addPoint(cxt.getNID(), new PVector(cxt.getPtPosition().getX(), cxt.getPtPosition().getY(), cxt.getPtPosition().getZ()));
    println("OnPointCreate, handId: " + cxt.getNID());
  }

  public void OnPointUpdate(XnVHandPointContext cxt) {
    //println("OnPointUpdate " + cxt.getPtPosition());   
    addPoint(cxt.getNID(), new PVector(cxt.getPtPosition().getX(), cxt.getPtPosition().getY(), cxt.getPtPosition().getZ()));
  }

  public void OnPointDestroy(long nID) {
    //println("OnPointDestroy, handId: " + nID);
    // remove list
    if (_pointLists.containsKey(nID))
      _pointLists.remove(nID);
  }

  public ArrayList getPointList(long handId) {
    ArrayList curList;
    if (_pointLists.containsKey(handId))
      curList = (ArrayList)_pointLists.get(handId);
    else {
      curList = new ArrayList(_maxPoints);
      _pointLists.put(handId, curList);
    }
    return curList;
  }

  public void addPoint(long handId, PVector handPoint) {
    ArrayList curList = getPointList(handId);
    curList.add(0, handPoint);      
    if (curList.size() > _maxPoints)
      curList.remove(curList.size() - 1);
  }

  public void draw() {
    if (_pointLists.size() <= 0)
      return;
    pushStyle();
    noFill();
    PVector vec;
    PVector firstVec;
    PVector screenPos = new PVector();
    int colorIndex=0;
    // draw the hand lists
    Iterator<Map.Entry> itrList = _pointLists.entrySet().iterator();
    while (itrList.hasNext ()) {
      strokeWeight(2);
      stroke(_colorList[colorIndex % (_colorList.length - 1)]);
      ArrayList curList = (ArrayList)itrList.next().getValue();     
      // draw line
      firstVec = null;
      Iterator<PVector> itr = curList.iterator();
      beginShape();
      while (itr.hasNext ()) {
        vec = itr.next();
        if (firstVec == null)
          firstVec = vec;
        // calc the screen pos
        context.convertRealWorldToProjective(vec, screenPos);
        vertex(screenPos.x, screenPos.y);
      } 
      endShape();   
      // draw current pos of the hand
      if (firstVec != null) {
        strokeWeight(8);
        context.convertRealWorldToProjective(firstVec, screenPos);
        point(screenPos.x, screenPos.y);
      }
      colorIndex++;
    }  
    popStyle();
  }
}

