package domain;

public abstract class BaseEntity {
    private Long id;

    public BaseEntity(Long id) {
        setId(id);
    }

    public void setId(Long id) {
        if(id==null || id>=0) {
            this.id= id;
        } else {
            throw new InvalidValueExeption("Kurs-ID muss größer gleich 0 sein");
        }
    }

    public Long getId() {
        return this.id;
    }

    @Override
    public String toString() {
        return "BaseEntity{" +
                "id=" + id +
                '}';
    }

}
