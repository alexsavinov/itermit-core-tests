package api.pojos;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Page {

    private int size;
    private int totalElements;
    private int totalPages;
    private int number;
}
