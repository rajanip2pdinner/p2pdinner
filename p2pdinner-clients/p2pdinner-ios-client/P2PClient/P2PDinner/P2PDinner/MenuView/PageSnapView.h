//
//  PageSnapView.h
//  P2PDinner
//
//  Created by Selvam M on 4/16/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import <UIKit/UIKit.h>

/**
 *  PageSnapViewDelegate is a protocol is invoked from
 */
@protocol PageSnapViewDelegate <NSObject>
/**
 *  This method is to get the index of selected image
 *
 *  @param index NSInteger index is passed as parameter
 */
- (void)selectedIndexWithIndex:(NSInteger)index;
/**
 *  This method is to show the image for given intex and given frame
 *
 *  @param index    NSInteger index as parameter
 *  @param pageRect CGRect pageRect to show in frame
 */
- (void)showImageWithIndex:(NSInteger)index andFrame:(CGRect)pageRect;
/**
 *  This method is to show the image for given intex and given frame and the selected image
 *
 *  @param index     NSInteger index as parameter
 *  @param pageRect CGRect pageRect to show in frame
 *  @param img      UIImage image
 */
- (void)showImageWithIndex:(NSInteger)index andFrame:(CGRect)pageRect image:(UIImage *)img;
/**
 *  This method is to update the page number in tool bar
 *
 *  @param index NSInteger index is given as parameter
 */
- (void)updateLabel:(NSInteger)index;
@end

/**
 *   PageSnapView is a UIScrollView  base class that manages a weeklyAds thumbnail scrollview in the screen.
 */
@interface PageSnapView : UIScrollView <UIScrollViewDelegate>
/**
 *  Property to hold the delegate of PageSnapViewDelegate
 */
@property(nonatomic,weak)id <PageSnapViewDelegate>pageDelegate;
- (id)initWithFrame:(CGRect)frame andData:(NSArray *)array;
/**
 *  This method is to set the content size for the scrollview
 *
 *  @param index NSInteger index is passed as input value
 */
- (void)setTheSelectedImage:(NSInteger)index;
-(void)setNextSelectedIndex;
- (void)selectPageWithIndex:(NSInteger)index;
@end
