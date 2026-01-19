import java.util.List;

public class Campaign {
    private final String id;
    private final String content;
    private final List<ChannelType> channels;

    public Campaign(String id, String content, List<ChannelType> channels) {
        this.id = id;
        this.content = content;
        this.channels = channels;
    }

    public String getContent() {
        return content;
    }

    public List<ChannelType> getChannels() {
        return channels;
    }
}