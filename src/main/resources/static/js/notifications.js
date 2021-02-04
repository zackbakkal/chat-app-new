$(document).ready(function() {

	var eventSource = new EventSource("/notifications/subscribe");

	eventSource.addEventListener("newMessage", function(event) {
		var message = JSON.parse(event.data);

		var sender = $("#" + message.senderRecipient.sender);

		if(sender.attr("data-clicked") === "true") {
			$("#messages-list").append('<li class="recipientMessage">' + message.text + '</li>');
			$('#messages').scrollTop($('#messages')[0].scrollHeight);
		} else {
			sender.addClass("new-message");
		}

		eventSource.addEventListener("error", function(event) {
			console.log("Error:" , event.currentTarget.readyState);
			if(event.currentTarget.readyState == EventSource.CLOSED) {
			} else {
				eventSource.close();
			}
		});

		window.onbeforeunload = function() {
			eventSource.close();
		};

	});

	eventSource.addEventListener("updateUsersList", function(event) {

		userOnlineStatus = JSON.parse(event.data);

		var username = userOnlineStatus.username;
		var online = userOnlineStatus.online;

		var newClass = (online) ? "online" : "offline";
		var oldClass = (newClass === "offline") ? "online" : "offline";

		$("#" + username).removeClass(oldClass);
		$("#" + username).addClass(newClass);

		var newUsersList = newClass + "-users";

		$("#" + username).appendTo($("#" + newUsersList));

    });


});