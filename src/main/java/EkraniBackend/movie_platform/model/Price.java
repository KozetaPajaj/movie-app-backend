package EkraniBackend.movie_platform.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Price {
    private double rent;
    private double purchase;

    public Price() {
    }

    public Price(double rent, double purchase) {
        this.rent = rent;
        this.purchase = purchase;
    }
}
