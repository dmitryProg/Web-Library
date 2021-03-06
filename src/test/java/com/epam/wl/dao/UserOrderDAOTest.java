package com.epam.wl.dao;

import com.epam.wl.entities.UserOrder;
import com.epam.wl.enums.UserOrderStatus;
import com.epam.wl.executor.Executor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SuppressWarnings("ConstantConditions")
class UserOrderDAOTest implements TestData {

    private UserOrderDAO userOrderDAO = UserOrderDAO.getInstance();

    @BeforeEach
    public void initDatabase() {
            Executor.resetTestDataSource();
    }

    @Test
    void testCreateUserOrder() throws SQLException {
        userOrderDAO.createNewUserOrder(uo6.getBook().getId(),uo6.getUser().getId());
        UserOrder actual = userOrderDAO.getUserOrderByID(uo6.getId()).get();
        assertThat(actual, is(uo6));
    }

    @Test
    void testSetOrderStatus() throws SQLException {
        UserOrder expectedUserOrder = new UserOrder(2, u2, b2, UserOrderStatus.CLOSED);;
        userOrderDAO.setUserOrderStatus(2, UserOrderStatus.CLOSED);
        UserOrder actualUserOrder = userOrderDAO.getUserOrderByID(2).get();

        assertThat(expectedUserOrder, is(actualUserOrder));
    }

    @Test
    void testGetAllUserOrders() throws SQLException {
        //List<UserOrder> expectedOrderList = userOrders;
        assertThat(userOrders, is(userOrderDAO.getAllUserOrders()));
    }

    @Test
    void testGetUserOrderByID() throws SQLException {
        //UserOrder expectedUserOrder = uo2;
        UserOrder actualUserOrder = userOrderDAO.getUserOrderByID(uo2.getId()).get();

        assertThat(uo2, is(actualUserOrder));
    }

    @Test
    void testGetUserOrderByStatus() throws SQLException {
        assertThat(inProgressUserOrders, is(userOrderDAO.getUserOrderByStatus(UserOrderStatus.IN_PROGRESS)));
    }

    @Test
    void testGetUserOrderByUserId() throws SQLException {
        List<UserOrder> expectedOrderList = new ArrayList<>();
        expectedOrderList.add(uo3);//3, u3, b3, UserOrderStatus.IN_PROGRESS)

        assertThat(expectedOrderList, is(userOrderDAO.getUserOrderByUserId(uo3.getUser().getId())));//.getId()
    }

    @Test
    void failToGetByWrongId() throws SQLException {
        assertThrows(NoSuchElementException.class, () -> userOrderDAO.getUserOrderByID(5345345).get());
    }

    @Test
    void failToGetByNegativeId() throws SQLException {
        assertThrows(NoSuchElementException.class, () -> userOrderDAO.getUserOrderByID(-1).get());
    }
}
