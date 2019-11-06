/* Handle session timeout */

function refreshSession(imgName) {
  var date = new Date();  
  var curr_hour = date.getHours();
  var curr_min = date.getMinutes();

  if(confirm(curr_hour+":"+curr_min+" WARNING: Your FNRS Web Session is about to expire in 20 minutes. Once your session expires, you will need to re-login and unsaved data will be lost. "+
  "Click OK to continue your current session for another 55 minutes.")) {
    myImg = document.getElementById(imgName);
    if (myImg) {
      myImg.src = myImg.src.replace(/\?.*$/, '?' + Math.random());
    }
    setTimeout("refreshSession('keepAliveIMG')", 2100000);
  }
}

function keepSessionAlive(contextPath) {  
  document.writeln('<img id="keepAliveIMG" width="1" height="1" src="'+contextPath+'/resources/imgs/dummy.gif?'+ Math.random()+'" />');
  setTimeout("refreshSession('keepAliveIMG')", 2100000);
}

