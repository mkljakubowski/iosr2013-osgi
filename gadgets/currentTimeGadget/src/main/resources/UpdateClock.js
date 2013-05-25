/**
 * Created with IntelliJ IDEA.
 * User: bursant
 * Date: 5/25/13
 * Time: 10:37 PM
 * To change this template use File | Settings | File Templates.
 */

function update_clock(){
    var currentTime = new Date();

    var hours = currentTime.getHours();
    var minutes = currentTime.getMinutes();
    var seconds = currentTime.getSeconds();

    hour_hand.rotate(30*hours+(minutes/2.5), 100, 100);
    minute_hand.rotate(6*minutes, 100, 100);
    second_hand.rotate(6*seconds, 100, 100);
}