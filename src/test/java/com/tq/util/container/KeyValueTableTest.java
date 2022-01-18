package com.tq.util.container;

import java.util.HashMap;
import java.util.Map;

import com.tommwq.jet.container.KeyValueTable;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class KeyValueTableTest {

        public static class User {
                public int id;
                public String email;
                public String name;
                
                public User(int id, String name, String email) {
                        this.id = id;
                        this.name = name;
                        this.email = email;
                }
        }
        
        static HashMap<Integer,User> addressBook = new HashMap();

        static void init() {
                addressBook.put(1, new User(1, "alice", "alice@163.com"));
                addressBook.put(2, new User(2, "bob", "bob@gmail.com"));
                addressBook.put(3, new User(3, "cook", "cook@yahoo.com"));
        }
        
        @Test
        void select() {                 
//                Map<Integer,User> yahooUsers = KeyValueTable.<Integer,User>select(
//                        addressBook,
//                        new Function<User,Boolean>() {
//                                @Override public Boolean apply(User user) {
//                                        return user.email.endsWith("@yahoo.com");
//                                }});
//
//                assertEquals(1, yahooUsers.size());
        }

        @Test
        void project() {
                Map<Integer,String> usernames = KeyValueTable.<Integer,User,String>project(addressBook, "name");
                assertEquals(3, usernames.size());
        }
}
