package com.fima.cardsui.objects;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.FrameLayout;

import com.fima.cardsui.R;

public abstract class Card extends AbstractCard {
	private OnCardSwiped onCardSwipedListener;
	private OnClickListener mListener;
	private OnLongClickListener mLongListener;
	private int mBackgroundResourceId = R.drawable.card_background_shadow;

	public Card() {

	}

	public Card(String title) {
		this.title = title;
	}

	public Card(String title, String desc) {
		this.title = title;
		this.desc = desc;
	}

	public Card(String title, int image) {
		this.title = title;
		this.image = image;
	}

	public Card(String title, String desc, int image) {
		this.title = title;
		this.desc = desc;
		this.image = image;
	}

	public Card(String titlePlay, String description, String color,
			String titleColor, Boolean hasOverflow, Boolean isClickable) {
		this.titlePlay = titlePlay;
		this.description = description;
		this.color = color;
		this.titleColor = titleColor;
		this.hasOverflow = hasOverflow;
		this.isClickable = isClickable;
	}

	public Card(String titlePlay, String description, int imageRes,
			String titleColor, Boolean hasOverflow, Boolean isClickable) {
		this.titlePlay = titlePlay;
		this.description = description;
		this.titleColor = titleColor;
		this.hasOverflow = hasOverflow;
		this.isClickable = isClickable;
		this.imageRes = imageRes;
	}

	@Override
	public View getView(Context context, boolean swipable) {
		return getView(context, false);
	}

	@Override
	public View getView(Context context) {
		View view = LayoutInflater.from(context).inflate(R.layout.item_card, null);
		view.setBackgroundResource(mBackgroundResourceId);

		try {
			((FrameLayout) view.findViewById(R.id.cardContent))
					.addView(getCardContent(context));
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

		return view;
	}

	public abstract View getCardContent(Context context);

	public OnClickListener getClickListener() {
		return mListener;
	}

	public void setOnClickListener(OnClickListener listener) {
		mListener = listener;
	}

	public OnLongClickListener getLongClickListener() {
		return mLongListener;
	}

	public void setOnLongClickListener(OnLongClickListener listener) {
		mLongListener = listener;

	}

	public void OnSwipeCard() {
		if (onCardSwipedListener != null)
			onCardSwipedListener.onCardSwiped(this);
		// TODO: find better implementation to get card-object's used content
		// layout (=> implementing getCardContent());
	}

	public OnCardSwiped getOnCardSwipedListener() {
		return onCardSwipedListener;
	}

	public void setOnCardSwipedListener(OnCardSwiped onEpisodeSwipedListener) {
		this.onCardSwipedListener = onEpisodeSwipedListener;
	}

	public void setBackgroundResource(int resid) {
		mBackgroundResourceId = resid;
	}

	protected int getFirstCardLayout() {
		return R.layout.item_play_card_empty_first;
	}

	public interface OnCardSwiped {
		public void onCardSwiped(Card card);
	}

}
