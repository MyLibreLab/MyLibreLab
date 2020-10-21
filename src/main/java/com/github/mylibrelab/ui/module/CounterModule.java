/*
 * Copyright (C) 2020 MyLibreLab
 * Based on MyOpenLab by Carmelo Salafia www.myopenlab.de
 * Copyright (C) 2004  Carmelo Salafia cswi@gmx.de
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.github.mylibrelab.ui.module;

import java.awt.*;

import javax.swing.*;

import org.jetbrains.annotations.NotNull;

import com.github.mylibrelab.action.*;
import com.github.mylibrelab.action.Action;
import com.github.mylibrelab.observable.Observable;
import com.github.mylibrelab.observable.ObservableInt;
import com.github.mylibrelab.observable.ObservableManager;
import com.github.mylibrelab.text.Text;
import com.github.mylibrelab.ui.border.Borders;
import com.github.mylibrelab.ui.icons.AllIcons;
import com.github.mylibrelab.ui.view.ViewFactory;
import com.github.mylibrelab.ui.view.ViewProvider;
import com.github.mylibrelab.ui.view.ViewProviderFor;
import com.github.weisj.darklaf.util.Alignment;
import com.github.weisj.darklaf.util.AlignmentExt;

@AppModule
public class CounterModule extends DefaultApplicationModule {

    private final CounterModel model = new CounterModel();

    public CounterModule() {
        super(Text.of("Counter Component"), AllIcons.LOGO, Alignment.NORTH_WEST);
        actionContext.put(CounterModel.class, model);
    }

    @Override
    protected @NotNull JComponent createComponent() {
        return ViewFactory.createView(model);
    }

    @ActionGroup
    public static class IncrementGroup extends AnActionGroup {
    }

    @Action
    @ParentGroup(groups = IncrementGroup.class)
    public static class IncrementAction extends AnAction {

        public IncrementAction() {
            getPresentation().setDisplayName(Text.of("Increment"));
        }

        @Override
        public void actionPerformed(@NotNull final ActionContext context) {
            var model = context.get(CounterModel.class);
            if (model != null) {
                model.setCounter(model.getCounter() + 1);
            }
        }
    }

    @Action
    @ParentGroup(groups = IncrementGroup.class)
    public static class DecrementAction extends AnAction {

        public DecrementAction() {
            getPresentation().setDisplayName(Text.of("Decrement"));
        }

        @Override
        public void actionPerformed(@NotNull final ActionContext context) {
            var model = context.get(CounterModel.class);
            if (model != null) {
                model.setCounter(model.getCounter() - 1);
            }
        }
    }

    @ViewProvider
    public static class CounterView implements ViewProviderFor<CounterModel> {

        @Override
        public JComponent createView(final CounterModel model) {
            var comp = new JPanel(new BorderLayout());
            var labelHolder = new JPanel(new GridBagLayout());
            var buttonPanel = Box.createHorizontalBox();

            var label = new JLabel(String.valueOf(model.getCounter()));
            model.<Integer>registerListener(CounterModel.KEY_COUNTER, (o, n) -> label.setText(String.valueOf(n)));
            labelHolder.add(label);

            var increment = ActionUtil.createButton(IncrementAction.class);
            var decrement = ActionUtil.createButton(DecrementAction.class);

            buttonPanel.add(Box.createHorizontalGlue());
            buttonPanel.add(increment);
            buttonPanel.add(Box.createHorizontalStrut(10));
            buttonPanel.add(decrement);
            buttonPanel.add(Box.createHorizontalGlue());
            buttonPanel.setBorder(Borders.mask(Borders.emptyBorder(5), AlignmentExt.BOTTOM));

            comp.add(labelHolder, BorderLayout.CENTER);
            comp.add(buttonPanel, BorderLayout.SOUTH);
            return comp;
        }
    }

    public static class CounterModel implements Observable<CounterModel> {

        public static final String KEY_COUNTER = "counter";

        private final ObservableManager<CounterModel> manager = new ObservableManager<>();
        private final ObservableInt counter = new ObservableInt(this, KEY_COUNTER);

        @Override
        public @NotNull ObservableManager<CounterModel> getManager() {
            return manager;
        }

        public int getCounter() {
            return counter.get();
        }

        public void setCounter(final int counter) {
            this.counter.set(counter);
        }
    }
}
