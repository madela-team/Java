package ru.madela;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ignite.marshalling.ByteArrayMarshaller;

public class RapidRepeatResponseMarshaller implements ByteArrayMarshaller<RapidRepeatJobResponse> {
    private static final ObjectMapper M = new ObjectMapper();

    @Override
    public byte[] marshal(RapidRepeatJobResponse obj) {
        try {
            return M.writeValueAsBytes(obj);
        } catch (Exception e) {
            throw new RuntimeException("Marshal error", e);
        }
    }

    @Override
    public RapidRepeatJobResponse unmarshal(byte[] bytes) {
        try {
            return M.readValue(bytes, RapidRepeatJobResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("Unmarshal error", e);
        }
    }
}
