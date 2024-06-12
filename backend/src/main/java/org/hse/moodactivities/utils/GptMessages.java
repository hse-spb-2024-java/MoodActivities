package org.hse.moodactivities.utils;

import java.util.ArrayList;
import java.util.List;

public class GptMessages {
    public GptMessages(GptMessage message) {
        this();
        addMessage(message);
    }

    public static class GptMessage {
        private Role role;
        private String content;

        public GptMessage(final Role role, final String content) {
            this.role = role;
            this.content = validateMessage(content);
        }

        public Role getRole() {
            return this.role;
        }

        public String getContent() {
            return this.content;
        }

        public static enum Role {
            assistant,
            system,
            user
        }

        @Override
        public String toString() {
            return "{" +
                    "\"role\": \"" + role +
                    "\", \"content\": \"" + content + "\"}";
        }

        private String validateMessage(String message) {
            String newMessage = message.trim();
            if (newMessage.isEmpty()) {
                newMessage = "Tell me that someone asked you a meaningless question and an error occurred.";
            }
            return newMessage;
        }
    }

    private List<GptMessage> messages;

    public List<GptMessage> getMessages() {
        return this.messages;
    }

    public GptMessage getMessage(int id) {
        return (id < messages.size() ? messages.get(id) : null);
    }

    public GptMessages() {
        this.messages = new ArrayList<>();
    }

    public GptMessages(GptMessage.Role role, String content) {
        this.messages = new ArrayList<>();
        this.messages.add(new GptMessage(role, content));
    }

    public void addMessage(GptMessage.Role role, String content) {
        this.messages.add(new GptMessage(role, content));
    }

    public void addMessage(GptMessage message) {
        this.messages.add(message);
    }

    @Override
    public String toString() {
        return messages.toString();
    }
}
