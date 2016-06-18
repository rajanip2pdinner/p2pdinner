//
//  PageSnapView.m
//  P2PDinner
//
//  Created by Selvam M on 4/16/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import "PageSnapView.h"


//#define kPageViewHeight 48.0
//#define kPageViewYCoord -((kPageViewHeight/2)+40)
//#define kPagePadding 3.0
//#define kPageViewWidth 54.0
#define kPageViewYCoord 0.0
#define kPagePadding 3.0
#define kPageViewWidth 54.0
#define kPageViewHeight 48.0





#define kBasePageTag        1000

@interface PageSnapView(){
    
    float viewMidCoordX;
    NSInteger currentSelectedIndex;
    NSInteger pageCount;
    NSInteger getIndex;
}
@property(nonatomic,strong)NSArray *pageArray;

- (void)pageSelectAction:(id)sender;

@end

@implementation PageSnapView

- (id)initWithFrame:(CGRect)frame andData:(NSArray *)array
{
    self = [super initWithFrame:frame];
    if (self) {
        // Initialization code
        self.pageArray = array;
        
        [self setupView];
        
    }
    return self;
}
/**
 *  This method is to create initial view set up
 */
- (void)setupView
{
    
    self.delegate = self;
    viewMidCoordX = [[NSNumber numberWithDouble:(self.frame.size.width / 2 )]floatValue];  //Calculate mid of scrollview
    currentSelectedIndex = 0;
    pageCount = [self.pageArray count]; //total number of pages
    double x = 0;
    
    
    for (int i = 0; i < pageCount; i++) {
        
        UIImageView *imageView = [[UIImageView alloc] initWithFrame:CGRectMake([[NSNumber numberWithDouble:x] floatValue] , (kPageViewYCoord), (kPageViewWidth), (kPageViewHeight))];
        imageView.opaque = YES;
        imageView.userInteractionEnabled = YES;
        imageView.backgroundColor = [UIColor colorWithRed:224.0/255.0 green:224.0/255.0 blue:224.0/255.0 alpha:1];
        [self addSubview:imageView];
        NSString *info =[self.pageArray objectAtIndex:i];
        imageView.image=[UIImage imageNamed:[NSString stringWithFormat:@"%@",info]];
        imageView.tag = kBasePageTag + i;
        if (i == 0) {
        }
        
        UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(pageSelectAction:)];
        [imageView addGestureRecognizer:tap];
        
        x += (kPagePadding) + (kPageViewWidth);
        [self selectPageWithIndex:0];
    }
    self.showsHorizontalScrollIndicator = NO;
    double contentSizeWidth = (((kPageViewWidth) + (kPagePadding)) * pageCount);
    
    self.contentSize = CGSizeMake([[NSNumber numberWithDouble:contentSizeWidth] floatValue], self.frame.size.height);
    
    
}
/**
 *  This method is to reset the scrollView position wrt the selected index from the WeeklyAdsImages  cover flow
 *
 *  @param index NSInteger index is given as parameter
 */
-(void)setTheSelectedImage:(NSInteger)index{
    
    UIImageView *imgeView = (UIImageView *)[self viewWithTag:(index-1)+kBasePageTag];
    CGRect imgViewFrame = imgeView.frame;
    imgViewFrame.origin.x=imgViewFrame.origin.x;
    
    [self calculateSnapForScrollView];
    
}
/**
 *  This method is to take to detail page of the selected image
 *
 *  @param tapGesture UITapGestureRecognizer tapGesture is passed as parameter
 */
- (void)pageSelectAction:(UITapGestureRecognizer *)tapGesture{
    
    [self selectPageWithIndex:(tapGesture.view.tag - kBasePageTag)];
    
}

#pragma mark - SCROLLVIEW DELEGATE METHODS
/**
 *  This is UIScrollview delegate method for scroll view did end dragging
 *
 *  @param scrollView UIScrollView Scrollview is the parameter here
 *  @param decelerate BOOL decelerate is passed as parameter
 */
- (void)scrollViewDidEndDragging:(UIScrollView *)scrollView willDecelerate:(BOOL)decelerate{
    
    if (!decelerate) {
        //[self calculateSnapForScrollView];
    }
    
}
/**
 *  This method is UIScrollView delegate method for scroll view did end Decelerating
 *
 *  @param scrollView UIScrollView Scrollview is the parameter here
 */
- (void)scrollViewDidEndDecelerating:(UIScrollView *)scrollView{
    
    
    // [self calculateSnapForScrollView];
}

/**
 *  Method to calculate Snap For ScrollView
 */
- (void)calculateSnapForScrollView{
    
    
    float midXOffset = [[NSNumber numberWithDouble:self.contentOffset.x + viewMidCoordX]floatValue];             //Center x coordinate of contentview of scrollview
    
    double pageStartingXCoord = viewMidCoordX - (kPageViewWidth/2);        //Starting x coordinate of page
    
    double indexValue = ((midXOffset - pageStartingXCoord)/((kPageViewWidth) + (kPagePadding)));
    
    NSInteger index = (int)round(indexValue) ;
    if (index >= pageCount) {
        
        index = pageCount-1;
        
    }
    
    if (index <= 0) {
        
        index = 0;
        
    }
    [self setBorder:index];
    [self.pageDelegate updateLabel:currentSelectedIndex];
    UIImageView *imageView = (UIImageView *)[self viewWithTag:kBasePageTag + currentSelectedIndex];
    NSString *info =[self.pageArray objectAtIndex:index];
    imageView.image = [UIImage imageNamed:[NSString stringWithFormat:@"%@S.png",info]];
    imageView.backgroundColor=[UIColor whiteColor];
    
}

/**
 *  This Method is to set border for selected thumbnail image
 *
 *  @param index NSInteger index is given as parameter
 */
- (void)setBorder:(NSInteger)index
{
    int oldSelectedIndex = (int)currentSelectedIndex;
    currentSelectedIndex = index;
    
//    UIImageView *imageView = (UIImageView *)[self viewWithTag:kBasePageTag + currentSelectedIndex];
//    int newOffsetX = [[NSNumber numberWithDouble:imageView.frame.origin.x] intValue];
//    newOffsetX -= (viewMidCoordX - (kPageViewWidth)/2);
    UIImageView *oldimageView = (UIImageView *)[self viewWithTag:kBasePageTag + oldSelectedIndex];
    [self setBlurViewForSelectedImage];
    NSString *info=[self.pageArray objectAtIndex:oldSelectedIndex];
    oldimageView.image = [UIImage imageNamed:[NSString stringWithFormat:@"%@.png",info]];
    oldimageView.backgroundColor = [UIColor colorWithRed:224.0/255.0 green:224.0/255.0 blue:224.0/255.0 alpha:1];
    getIndex=index;
}
/**
 *  This Method is to make the selected image blur
 */
- (void)setBlurViewForSelectedImage{
    
    for (int i = 0 ; i < pageCount; i++) {
    }
}
/**
 Method to select page
 @param: index : send NSInteger as parameter
 */
/**
 *  This method is to set the page number in detail page
 *
 *  @param index NSInteger index is passed as parameter
 */
- (void)selectPageWithIndex:(NSInteger)index{
    
    
    int oldSelectedIndex = (int)currentSelectedIndex;
    currentSelectedIndex = index;
    
    UIImageView *imageView = (UIImageView *)[self viewWithTag:kBasePageTag + currentSelectedIndex];
    
    //int newOffsetX =[[NSNumber numberWithDouble:imageView.frame.origin.x] intValue];
    //newOffsetX -= (viewMidCoordX - (kPageViewWidth)/2);
    // make it fixed
    // [self setContentOffset:CGPointMake(newOffsetX, self.contentOffset.y) animated:YES];
    UIImageView *oldimageView = (UIImageView *)[self viewWithTag:kBasePageTag + oldSelectedIndex];
    NSString *info =[self.pageArray objectAtIndex:oldSelectedIndex];
    oldimageView.image=[UIImage imageNamed:[NSString stringWithFormat:@"%@.png",info]];
    oldimageView.backgroundColor=[UIColor colorWithRed:224.0/255.0 green:224.0/255.0 blue:224.0/255.0 alpha:1];
    NSString *info1 =[self.pageArray objectAtIndex:currentSelectedIndex];
    imageView.image=[UIImage imageNamed:[NSString stringWithFormat:@"%@S.png",info1]];
    imageView.backgroundColor=[UIColor whiteColor];
    [self.pageDelegate updateLabel:currentSelectedIndex];
    [self setBlurViewForSelectedImage];
}
- (void)setNextSelectedIndex{
    
    if (currentSelectedIndex<([self.pageArray count]-1)) {
        NSInteger curentIndex=currentSelectedIndex;
        [self selectPageWithIndex:curentIndex+1];
    }
}
@end
