package com.fima.cardsui.objects;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fima.cardsui.R;
import com.fima.cardsui.StackAdapter;
import com.fima.cardsui.SwipeDismissTouchListener;
import com.fima.cardsui.SwipeDismissTouchListener.OnDismissCallback;
import com.fima.cardsui.Utils;
import com.nineoldandroids.animation.ObjectAnimator;

public class CardStack extends AbstractCard {
	private static final float _45F = 45f;
	private static final String NINE_OLD_TRANSLATION_Y = "translationY";
	private ArrayList<Card> cards;
	private String title, stackTitleColor;
	private int selectedCardIndex = -1;

	private StackAdapter mAdapter;
	private int mPosition;
	private Context mContext;
	private CardStack mStack;

	public CardStack() {
		cards = new ArrayList<Card>();
		mStack = this;
	}

	public CardStack(String title) {
		cards = new ArrayList<Card>();
		mStack = this;

		setTitle(title);
	}

	public ArrayList<Card> getCards() {
		return cards;
	}

	public void add(Card newCard) {
		cards.add(newCard);

	}

	@Override
	public View getView(Context context) {
		return getView(context, false);
	}

	@Override
	public View getView(Context context, boolean swipable) {
		mContext = context;

		final View view = LayoutInflater.from(context).inflate(R.layout.item_stack,
				null);
		final RelativeLayout container = (RelativeLayout) view
				.findViewById(R.id.stackContainer);
		final TextView title = (TextView) view.findViewById(R.id.stackTitle);

		if (!TextUtils.isEmpty(this.title)) {
			if (stackTitleColor == null) {
				stackTitleColor = context.getResources().getString(
						R.color.card_title_text);
			}

			title.setTextColor(Color.parseColor(stackTitleColor));
			title.setText(this.title);
			title.setVisibility(View.VISIBLE);
		}

		Card card;
		View cardView;
		for (int i = 0; i < cards.size(); i++) {
			cardView = null;
			int topPx = 0;
			card = cards.get(i);

			cardView = card.getView(context);
			cardView.setOnClickListener(getClickListener(this, container, i));

			if (i > 0) {
				float dp = _45F * i;
				topPx = Utils.convertDpToPixelInt(context, dp);
			}

			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.MATCH_PARENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			lp.setMargins(0, topPx, 0, 0);
			cardView.setLayoutParams(lp);

			if (swipable) {
				cardView.setOnTouchListener(new SwipeDismissTouchListener(cardView,
						card, new OnDismissCallback() {

							@Override
							public void onDismiss(View view, Object token) {
								Card c = (Card) token;
								// call onCardSwiped() listener
								c.OnSwipeCard();
								cards.remove(c);

								mAdapter.setItems(mStack, getPosition());

								// refresh();
								mAdapter.notifyDataSetChanged();

							}
						}));
			}

			container.addView(cardView);
		}

		return view;
	}

	public Card remove(int index) {
		return cards.remove(index);
	}

	public Card get(int i) {
		return cards.get(i);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String setColor(String color) {
		return this.stackTitleColor = color;
	}

	private OnClickListener getClickListener(final CardStack cardStack,
			final RelativeLayout container, final int index) {
		return new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (selectedCardIndex < 0) {
					int indexHeight = container.getChildAt(index).getHeight()
							- convertDpToPixel(45);

					for (int i = index + 1; i < container.getChildCount(); i++) {
						ObjectAnimator anim = ObjectAnimator
								.ofFloat(container.getChildAt(i), NINE_OLD_TRANSLATION_Y, 0,
										indexHeight);
						anim.start();
					}

					selectedCardIndex = index;
				} else if (index == selectedCardIndex) {
					int indexHeight = container.getChildAt(index).getHeight()
							- convertDpToPixel(45);

					for (int i = index + 1; i < container.getChildCount(); i++) {
						ObjectAnimator anim = ObjectAnimator
								.ofFloat(container.getChildAt(i), NINE_OLD_TRANSLATION_Y,
										indexHeight, 0);
						anim.start();
					}

					selectedCardIndex = -1;
				} else if (index < selectedCardIndex) {
					int oldIndexHeight = container.getChildAt(selectedCardIndex)
							.getHeight() - convertDpToPixel(45);
					int indexHeight = container.getChildAt(index).getHeight()
							- convertDpToPixel(45);

					for (int i = index + 1; i <= selectedCardIndex; i++) {
						ObjectAnimator anim = ObjectAnimator
								.ofFloat(container.getChildAt(i), NINE_OLD_TRANSLATION_Y, 0,
										indexHeight);
						anim.start();
					}

					for (int i = selectedCardIndex + 1; i < container.getChildCount(); i++) {
						ObjectAnimator anim = ObjectAnimator.ofFloat(
								container.getChildAt(i), NINE_OLD_TRANSLATION_Y,
								oldIndexHeight, indexHeight);
						anim.start();
					}

					selectedCardIndex = index;
				} else {
					int oldIndexHeight = container.getChildAt(selectedCardIndex)
							.getHeight() - convertDpToPixel(45);
					int indexHeight = container.getChildAt(index).getHeight()
							- convertDpToPixel(45);

					for (int i = selectedCardIndex + 1; i <= index; i++) {
						ObjectAnimator anim = ObjectAnimator.ofFloat(
								container.getChildAt(i), NINE_OLD_TRANSLATION_Y,
								oldIndexHeight, 0);
						anim.start();
					}

					for (int i = index + 1; i < container.getChildCount(); i++) {
						ObjectAnimator anim = ObjectAnimator.ofFloat(
								container.getChildAt(i), NINE_OLD_TRANSLATION_Y,
								oldIndexHeight, indexHeight);
						anim.start();
					}

					selectedCardIndex = index;
				}
			}
		};
	}

	protected int convertDpToPixel(float dp) {
		return (int) Utils.convertDpToPixel(mContext, dp);
	}

	public void setAdapter(StackAdapter stackAdapter) {
		mAdapter = stackAdapter;

	}

	public void setPosition(int position) {
		mPosition = position;
	}

	public int getPosition() {
		return mPosition;
	}

}
