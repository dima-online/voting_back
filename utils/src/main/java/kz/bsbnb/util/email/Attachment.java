package kz.bsbnb.util.email;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class Attachment {
	@NonNull
	private String name;

	@NonNull
	private byte[] bytes;
}