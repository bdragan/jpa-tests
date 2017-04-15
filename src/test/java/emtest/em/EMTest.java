package emtest.em;

import emtest.IntegrationTestsConfiguration;
import emtest.model.Detail;
import emtest.model.Master;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = IntegrationTestsConfiguration.class)
@Transactional
@Rollback
public class EMTest {

    @Autowired
    private EntityManager em;

    @Test
    public void merge() {
        Master master = new Master();
        Detail detail1 = new Detail("detail 1");
        master.addDetail(detail1);
        master = em.merge(master);
        flushAndClear();

        Detail detail2 = new Detail("detail 2");
        master.addDetail(detail2);
        master = em.merge(master);
        assertThat(master.getDetails(), containsInAnyOrder(detail1, detail2));
        flushAndClear();

        final String detail1Query = "select d from Detail d where d.name = 'detail 1'";
        assertThat(em.createQuery(detail1Query, Detail.class).getResultList(), hasSize(1));
        master.getDetails().remove(detail1);
        master = em.merge(master);
        assertThat(master.getDetails(), containsInAnyOrder(detail2));
        flushAndClear();
        assertThat(em.createQuery(detail1Query, Detail.class).getResultList(), empty());

        Detail detail3 = new Detail("detail 3");
        master.addDetail(detail3);
        master = em.merge(master);
        assertThat(master.getDetails(), containsInAnyOrder(detail2, detail3));
        flushAndClear();

        assertThat(em.find(Master.class, master.getId()).getDetails(),
                containsInAnyOrder(detail2, detail3));
    }


    /**
     * Flushes the current changes to the db and clears the persistence context,
     * making all persistent instances detached.
     */
    private void flushAndClear() {
        em.flush();
        em.clear();
    }
}
