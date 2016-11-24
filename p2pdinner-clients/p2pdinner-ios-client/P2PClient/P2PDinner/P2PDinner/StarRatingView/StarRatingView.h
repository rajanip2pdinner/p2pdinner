//
//  StarRatingView.h
//  StarRatingDemo
//
//  Created by HengHong on 5/4/13.
//  Copyright (c) 2013 Fixel Labs Pte. Ltd. All rights reserved.
//


#import <UIKit/UIKit.h>

@protocol RatingDelegate<NSObject>
- (void)updatedRatingValue:(int)ratingValue
                withCartId:(NSString *)cartId;
@end

@interface StarRatingView : UIView
@property (nonatomic) int userRating;
@property (nonatomic) int maxrating;
@property (nonatomic) int rating;

@property (nonatomic) BOOL animated;

@property (nonatomic) float kLabelAllowance;

@property (nonatomic,strong) NSTimer    *timer;
@property (nonatomic,strong) UILabel    *label;
@property (nonatomic,strong) NSString   *cartId;

@property (nonatomic,weak)id<RatingDelegate> delegate;

- (id)initWithFrame:(CGRect)frame
          andRating:(int)rating
          withLabel:(BOOL)label
           animated:(BOOL)animated;

@end
