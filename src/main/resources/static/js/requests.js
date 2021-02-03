var currentConversation;
var recipient;
var tabIndex = 1;

$(document).ready(function () {

	$("#messages-list").append('<li>ChatApp Messages ...</li>');
	$("#messages-list").addClass("welcome");

	getOnlineUsers();

    getOfflineUsers();

    $("#send").click(function (event) {
        event.preventDefault();
        var text = $("#sentmessage").val();
        if(text !== "" && recipient != null) {
        	sendMessage(text, recipient);
        }
    });

});


function getOnlineUsers() {
	$.ajax({
            type: "GET",
            url: "users/online",
            success: function (onlineUsers) {

                $("#online-users").empty();
                $.each(onlineUsers, function (index, onlineUser) {

                    $("#online-users").append('<div id="' + onlineUser.username + '"></div>');

                    $("#" + onlineUser.username).addClass("user");
                    $("#" + onlineUser.username).addClass("online");
    				$("#" + onlineUser.username).text(onlineUser.username);
    				$("#" + onlineUser.username).attr("tabIndex", tabIndex++);

                    $("#" + onlineUser.username).click(function () {
    					if(currentConversation !== null) {
                            $(currentConversation).attr("data-clicked", false);
                            $(currentConversation).removeClass("focus");
                        }

                        $("#" + onlineUser.username).attr("data-clicked", "true");
                        $("#" + onlineUser.username).removeClass("new-message");
                        currentConversation = $("#" + onlineUser.username);
                        $(currentConversation).addClass("focus");
                        recipient = onlineUser.username;
                        loadConversation(onlineUser.username);
                    });
                });
            },
            error: function (e) {
                $("#online-users").append('<div>?</div>');
            },
        });
}

function getOfflineUsers() {
	$.ajax({
            type: "GET",
            url: "users/offline",
            success: function (offlineUsers) {
				console.log(offlineUsers);
                $("#offline-users").empty();
                $.each(offlineUsers, function (index, offlineUser) {
					console.log(tabIndex + " " + offlineUser.username);
                    $("#offline-users").append('<div id="' + offlineUser.username + '"></div>');

                    $("#" + offlineUser.username).addClass("user");
                    $("#" + offlineUser.username).addClass("offline");
    				$("#" + offlineUser.username).text(offlineUser.username);
    				$("#" + offlineUser.username).attr("tabIndex", tabIndex++);


                    $("#" + offlineUser.username).click(function () {
                        if(currentConversation !== null) {
                            $(currentConversation).attr("data-clicked", false);
                            $(currentConversation).removeClass("focus");
                        }

                        $("#" + offlineUser.username).attr("data-clicked", "true");
                        $("#" + offlineUser.username).removeClass("new-message");
                        currentConversation = $("#" + offlineUser.username);
                        $(currentConversation).addClass("focus");
                        recipient = offlineUser.username;
                        loadConversation(offlineUser.username);
                    });
                });
            },
            error: function (e) {
                $("#offline-users").append('<div>?</div>');
            },
        });
}

function loadConversation(username) {

    	$("#sentmessage").val("");
    	$("#sentmessage").data("emojioneArea").setText("");

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
          },
          error: function (e) {
            console.log("error");
            $("#messages-list").append(e);
          },
        });
}

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
            $("#sentmessage").data("emojioneArea").setText("");
            $('#messages').scrollTop($('#messages')[0].scrollHeight);
        },
        error: function (e) {
            $("#messages-list").append('<li class="send-error">Message could not be sent</li>');
        },

    });
}
