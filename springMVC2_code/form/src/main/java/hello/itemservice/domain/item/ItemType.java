package hello.itemservice.domain.item;

public enum ItemType {
    //BOOK 이름과 이것에 대한 ("도서") 설명

    BOOK("도서"), FOOD("음식"), ETC("기타");

    private final String description;

    ItemType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
