var currentConversation = null;
var recipient;
var tabIndex;

$(document).ready(function () {
  $("#remove-icon").click(function () {
    var username = $("#searched-username").val();
    if (username !== "") {
      $("#searched-username").val("");
      getOnlineUsers();
      getOfflineUsers();
    }
  });

  $("#searched-username").click(function () {
    $("#searched-username").removeClass("warning");
  });

  $("#searched-username").keyup(function () {
    $("#searched-username").removeClass("warning");
    var username = $("#searched-username").val();
    if (username !== "") {
      searchUser(username);
    } else {
      getOnlineUsers();
      getOfflineUsers();
      $("#searched-username").removeClass("warning");
    }
  });

  function searchUser(username) {
    tabIndex = 1;

    $.ajax({
      type: "GET",
      url: "users/startwith/" + username,
      success: function (users) {
        $("#online-users").empty();
        $("#offline-users").empty();

        $.each(users, function (index, user) {
          listUser(user);
        });
      },
      error: function (e) {
        $("#online-users").empty();
        $("#offline-users").empty();
        getOnlineUsers();
        getOfflineUsers();
        $("#searched-username").addClass("warning");
      },
    });

    function listUser(user) {
      var status = user.online ? "online" : "offline";

      $("#" + status + "-users").append(
        '<div id="' + user.username + '"></div>'
      );
      $("#" + user.username).addClass("user");
      $("#" + user.username).addClass(status);
      $("#" + user.username).text(user.username);
      $("#" + user.username).attr("tabIndex", tabIndex++);

      $("#" + user.username).click(function () {
        if (
          currentConversation !== null &&
          currentConversation.attr("id") !== user.username
        ) {
          $(currentConversation).attr("data-clicked", false);
          $(currentConversation).removeClass("focus");
        }

        $("#" + user.username).attr("data-clicked", "true");
        $("#" + user.username).removeClass("new-message");
        currentConversation = $("#" + user.username);
        $(currentConversation).addClass("focus");
        recipient = user.username;
        localStorage.setItem("recipient", recipient);
        loadConversation(user.username);
      });
    }
  }
});
