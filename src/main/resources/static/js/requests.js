var currentConversation;
var recipient;

$(document).ready(function () {
    $.ajax({
        type: "GET",
        url: "users/online",
        success: function (onlineUsers) {
            $("#online-users").empty();
            $.each(onlineUsers, function (index, onlineUser) {
                $("#online-users").append('<div class="user online" id="' + onlineUser.username + '">'
                                                + onlineUser.username + '</div>');
                $("#" + onlineUser.username).attr("class", "user online");

                $("#" + onlineUser.username).click(function () {
                    if(currentConversation !== null) {
                        $(currentConversation).attr("data-clicked", false);
                    }

                    $("#" + onlineUser.username).attr("data-clicked", "true");
                    currentConversation = $("#" + onlineUser.username);
                    recipient = onlineUser.username;
                    loadConversation(onlineUser.username);
                });
            });
        },
        error: function (e) {
            $("#online-users").append('<div>?</div>');
        },
    });

    $.ajax({
        type: "GET",
        url: "users/offline",
        success: function (offlineUsers) {
            $("#offline-users").empty();
            $.each(offlineUsers, function (index, offlineUser) {
                $("#offline-users").append('<div id="' + offlineUser.username + '"  class="user offline">'
                                                    + offlineUser.username + '</div>');

                $("#" + offlineUser.username).click(function () {
                    if(currentConversation !== null) {
                        $(currentConversation).attr("data-clicked", false);
                    }

                    $("#" + offlineUser.username).attr("data-clicked", "true");
                    currentConversation = $("#" + offlineUser.username);
                    recipient = offlineUser.username;
                    loadConversation(offlineUser.username);
                });
            });
        },
        error: function (e) {
            $("#offline-users").append('<div>?</div>');
        },
    });

    $("#send").click(function (event) {
        event.preventDefault();
        var text = $("#sentmessage").val();
        sendMessage(text, recipient);
      });

    function sendMessage(text, recipient) {
        $.ajax({
            type: "POST",
            url: "messages/sendMessage",
            data: JSON.stringify({
                        recipient: recipient,
                        text: text
            }),
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function (messageSent) {
                $("#messages-list").append('<li class="senderMessage">' + messageSent.text + '</li>');
                $("#sentmessage").val("");
                $('#messages').scrollTop($('#messages')[0].scrollHeight);
            },
            error: function (e) {
                $("#messages-list").append('<li class="send-error">Message could not be sent</li>');
            },

        });
    }

    function loadConversation(username) {
        $.ajax({
          type: "GET",
          url: "conversations/" + username,
          success: function (conversation) {
            $("#messages-list").empty();

            $.each(conversation, function (index, message) {
                var messageStyle = "recipientMessage";
                if(message.senderRecipient.recipient === username) {
                    messageStyle = "senderMessage";
                }
              $("#messages-list").append('<li class="' + messageStyle + '">' + message.text + '</li>');
            });

            $('#messages').scrollTop($('#messages')[0].scrollHeight);
            //$("button[type=submit][clicked=true]").prop("clicked", false);
          },
          error: function (e) {
            console.log("error");
            $("#messages-list").append(e);
          },
        });
    }

    // TODO: refresh messages when new ones are available

});