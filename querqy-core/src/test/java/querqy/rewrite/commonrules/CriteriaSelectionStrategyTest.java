package querqy.rewrite.commonrules;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertEquals;
import static querqy.rewrite.commonrules.PrimitiveValueSelectionStrategyFactory.criteriaToJsonPathExpressionCriterion;
import static querqy.rewrite.commonrules.model.InstructionsTestSupport.instructions;

import org.hamcrest.Matchers;
import org.junit.Test;
import querqy.rewrite.commonrules.model.Criteria;
import querqy.rewrite.commonrules.model.CriteriaSelectionStrategy;
import querqy.rewrite.commonrules.model.FilterCriterion;
import querqy.rewrite.commonrules.model.Instructions;
import querqy.rewrite.commonrules.model.Sorting;
import querqy.rewrite.commonrules.model.TopRewritingActionCollector;

import java.util.Collections;
import java.util.Comparator;

public class CriteriaSelectionStrategyTest {

    @Test
    public void testThatInstructionsOrdIsDefaultSortOrder() {
        final CriteriaSelectionStrategy strategy =
                new CriteriaSelectionStrategy(new Criteria(null, 1, Collections.emptyList()));

        final Comparator<Instructions> comparator = strategy.getSortingComparator();

        final Instructions instructions1 = instructions(1);
        final Instructions instructions2 = instructions(2);

        assertEquals(0, comparator.compare(instructions1, instructions1));
        assertEquals(0, comparator.compare(instructions2, instructions2));
        assertThat(comparator.compare(instructions1, instructions2), Matchers.lessThan(0));
        assertThat(comparator.compare(instructions2, instructions1), Matchers.greaterThan(0));

    }

    @Test
    public void testThatComparatorLimitAndFiltersArePassedToTopRewritingActionCollector() {
        final Sorting sorting = new Sorting("name1", Sorting.SortOrder.DESC);
        final FilterCriterion filter = criteriaToJsonPathExpressionCriterion("f1:v1");
        final CriteriaSelectionStrategy strategy =
                new CriteriaSelectionStrategy(new Criteria(sorting, 17, Collections.singletonList(filter)));

        final TopRewritingActionCollector collector = strategy.createTopRewritingActionCollector();

        assertEquals(sorting, collector.getComparator());
        assertEquals(17, collector.getLimit());
        assertThat(collector.getFilters(), contains(filter));
    }

}
