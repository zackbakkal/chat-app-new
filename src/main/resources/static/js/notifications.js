$(document).ready(function () {
  var eventSource = new EventSource("/notifications/subscribe");

  eventSource.addEventListener("newMessage", function (event) {
    var message = JSON.parse(event.data);

    var sender = $("#" + message.senderRecipient.sender);

    if (sender.attr("data-clicked") === "true") {
      $("#messages-list").append(
        '<li class="recipientMessage">' + message.text + "</li>"
      );
      $("#messages").scrollTop($("#messages")[0].scrollHeight);
    } else {
      sender.addClass("new-message");
    }

    eventSource.addEventListener("error", function (event) {
      console.log("Error:", event.currentTarget.readyState);
      if (event.currentTarget.readyState == EventSource.CLOSED) {
      } else {
        eventSource.close();
      }
    });

    window.onbeforeunload = function () {
      eventSource.close();
    };
  });

  eventSource.addEventListener("updateUsersList", function (event) {
    userOnlineStatus = JSON.parse(event.data);

    var username = userOnlineStatus.username;
    var online = userOnlineStatus.online;
    var availability = userOnlineStatus.availability;

    var newClass = online ? "online" : "offline";

    if (!online) {
      $("#" + username).attr("class", "user " + newClass);
    } else {
      $("#" + username).attr("class", "user " + availability);
    }

    var newUsersList = newClass + "-users";

    $("#" + username).appendTo($("#" + newUsersList));
  });

  eventSource.addEventListener("updateAvailability", function (event) {
    userAvailability = JSON.parse(event.data);

    var username = userAvailability.username;
    var availability = userAvailability.availability;

    $("#" + username).attr("class", "user " + availability);

    var newClass =
      availability === "available"
        ? "fa fa-circle fa-xs"
        : availability === "away"
        ? "fa fa-circle-o fa-xs"
        : "fa fa-circle-o-notch fa-xs";

    $("#" + username + " i").attr("class", newClass);
  });

  eventSource.addEventListener("updateAvatar", function (event) {
    userAvatar = JSON.parse(event.data);

    var username = userAvatar.username;
    var hasAvatar = userAvatar.hasAvatar;

    $("#" + username + "-profile-image").attr(
      "src",
      "users/download/image/" + username
    );

    if (!hasAvatar) {
      userProfileImage =
        '<img id="' +
        user.username +
        '-profile-image" src="images/avatar.svg" class="avatar"/>';
    }
  });
});
