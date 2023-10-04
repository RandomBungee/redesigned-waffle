package de.jokileda.core.api.proxy.punish;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PunishEntry {
    private String uuid;
    private String reason;
    private String author;
    private long time;
    private PunishType punishType;
    private static final Builder BUILDER = new Builder();

    public static Builder newBuilder() {
        return BUILDER;
    }

    @Getter
    @NoArgsConstructor
    public static class Builder {
        private String uuid;
        private String reason;
        private String author;
        private long time;
        private PunishType punishType;

        public PunishEntry build() {
            PunishEntry result = new PunishEntry();
            result.uuid = uuid;
            result.reason = reason;
            result.author = author;
            result.time = time;
            result.punishType = punishType;
            return result;
        }

        public Builder setUuid(String uuid) {
            this.uuid = uuid;
            return this;
        }

        public Builder setReason(String reason) {
            this.reason = reason;
            return this;
        }

        public Builder setAuthor(String author) {
            this.author = author;
            return this;
        }

        public Builder setTime(long time) {
            this.time = time;
            return this;
        }

        public Builder setPunishType(PunishType punishType) {
            this.punishType = punishType;
            return this;
        }
    }
}
