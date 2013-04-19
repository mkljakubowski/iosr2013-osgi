package pl.edu.agh.istuff.social;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.amdatu.opensocial.spi.BackendException;
import org.amdatu.opensocial.spi.PersonBackendService;
import org.amdatu.opensocial.spi.QueryOptions;
import org.apache.shindig.auth.AnonymousSecurityToken;
import org.apache.shindig.social.core.model.AddressImpl;
import org.apache.shindig.social.core.model.ListFieldImpl;
import org.apache.shindig.social.core.model.NameImpl;
import org.apache.shindig.social.core.model.PersonImpl;
import org.apache.shindig.social.opensocial.model.Address;
import org.apache.shindig.social.opensocial.model.ListField;
import org.apache.shindig.social.opensocial.model.Person;

/**
 * Provides a demo implementation of {@link PersonBackendService}.
 */
public class DemoPersonService implements PersonBackendService {

    @Override
    public int countPeople(Collection<String> userIds, String groupId, Collection<String> fields, QueryOptions options) throws BackendException {
        return 1;
    }

    @Override
    public Collection<Person> getPeople(Collection<String> ids, String group,
                                        Collection<String> fields, QueryOptions options) throws BackendException {
        List<Person> result = new ArrayList<Person>();
        Person anonymous = new PersonImpl();
        anonymous.setId(AnonymousSecurityToken.ANONYMOUS_ID);
        anonymous.setName(new NameImpl("anoniempje"));
        anonymous.setNickname("anoniempje");

        Address addr1 = new AddressImpl();
        addr1.setCountry("NL");
        addr1.setPostalCode("6511LH");
        addr1.setStreetAddress("Ziekerstraat 10");

        Address addr2 = new AddressImpl();
        addr2.setCountry("NL");
        addr2.setPostalCode("3825JD");
        addr2.setStreetAddress("Duifpolder 18");

        anonymous.setCurrentLocation(addr2);
        anonymous.setDisplayName("JaWi");

        ListField field1 = new ListFieldImpl();
        field1.setPrimary(Boolean.TRUE);
        field1.setType("home");
        field1.setValue("j.w.janssen@lxtreme.nl");

        ListField field2 = new ListFieldImpl();
        field2.setPrimary(Boolean.FALSE);
        field2.setType("work");
        field2.setValue("janwillem.janssen@luminis.eu");

        anonymous.setEmails(Arrays.asList(field1, field2));

        anonymous.setAddresses(Arrays.asList(addr1, addr2));

        result.add(anonymous);
        return result;
    }

    @Override
    public Person getPerson(String id, Collection<String> fields) throws BackendException {
        if (id != null && AnonymousSecurityToken.ANONYMOUS_ID.equals(id)) {
            Person anonymous = new PersonImpl();
            anonymous.setId(AnonymousSecurityToken.ANONYMOUS_ID);
            anonymous.setName(new NameImpl("anoniempje"));
            anonymous.setNickname("anoniempje");
            return anonymous;
        }
        else if (id != null) {
            Person person = new PersonImpl();
            person.setId(id);
            person.setName(new NameImpl(id));
            person.setNickname(id);
            return person;
        }
        else {
            throw new BackendException("Person not found!");
        }
    }

    @Override
    public Person updatePerson(String id, Person person) throws BackendException {
        return person;
    }
}