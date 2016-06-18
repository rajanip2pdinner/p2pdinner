//
//  ActivityView.m
//  P2PDinner
//
//  Created by Selvam M on 3/15/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import "ActivityView.h"
#import "Utility.h"

#define kSpinnerWidth   40
#define kSpinnerHeight  40
#define kActivtyProgressViewWidth 40
#define kActivtyProgressViewHeight 40
@interface ActivityView()
{
    UIView *activityProgressView;
}
@end
@implementation ActivityView
- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        // Initialization code
        [self initializeViewWithFrame:frame];
    }
    return self;
}
- (void)deviceOrientationDidChange:(NSNotification *)notification {
    //Obtain current device orientation
    //UIDeviceOrientation orientation = [[UIDevice currentDevice] orientation];
    self.frame=CGRectMake(self.frame.origin.x,self.frame.origin.y,[[UIScreen mainScreen] bounds].size.width,[[UIScreen mainScreen] bounds].size.height);
    //NSLog(@"Changed %@",self.bounds);    //Do my thing
}
- (void)initializeViewWithFrame:(CGRect)frame
{
    [[NSNotificationCenter defaultCenter] addObserver: self selector:@selector(deviceOrientationDidChange:) name: UIDeviceOrientationDidChangeNotification object: nil];
    //UIView Setup
    UIView *view =[[UIView alloc]init];
    view.backgroundColor = [UIColor colorWithWhite:0.0 alpha:0.5];
    view.layer.cornerRadius = 9;
    view.layer.masksToBounds = YES;
    _activityViewBorder=[[UILabel alloc]init];
    [_activityViewBorder setBackgroundColor:[UIColor colorWithWhite:0.0 alpha:0.5]];
    //Label Setup
    _activityLabel = [[UILabel alloc] init];
    _activityLabel.textAlignment =  NSTextAlignmentCenter;
    _activityLabel.backgroundColor = [UIColor clearColor];
    _activityLabel.numberOfLines=0;
    _activityLabel.textColor = [UIColor whiteColor];
    
    //ActivityIndicator Setup
    _activityIndicatorView = [[UIActivityIndicatorView alloc]
                              initWithActivityIndicatorStyle:UIActivityIndicatorViewStyleWhite];
    
    [view addSubview:_activityIndicatorView];
    [view addSubview:_activityLabel];
    [self addSubview:view];
    activityProgressView = view;
    
    view = nil;
    self.hidden = YES;
}
/*
 Method alignProgressIndicator
 @param: progressText send NSString as parameter.
 given text is showing with indicator
 */
- (void)alignProgressIndicator:(NSString *)progressText
{
    _activityIndicatorView.frame = CGRectMake(0,0,kSpinnerWidth, kSpinnerHeight);
    
    _activityLabel.text =progressText;
    //Sizeof Warning fixed
    CGSize size =[Utility getSizeOfString:_activityLabel.text OfFont:_activityLabel.font];
    
    _activityLabel.frame = CGRectMake(0,0, size.width, size.height);
    
    
    if ([progressText length]>0)
    {
        CGFloat viewHeight =  _activityIndicatorView.frame.size.height + _activityLabel.frame.size.height + 5;
        
        activityProgressView.frame = CGRectMake((self.frame.size.width * 0.5f - size.width * 0.5f)-(kActivtyProgressViewWidth*0.5f), (self.frame.size.height * 0.5f - viewHeight * 0.5f)-(kActivtyProgressViewWidth*0.5f), size.width+kActivtyProgressViewWidth, viewHeight+kActivtyProgressViewWidth);
        
        CGRect activityIndicatorFrame =_activityIndicatorView.frame;
        activityIndicatorFrame.origin.x = (CGRectGetWidth( activityProgressView.frame) * 0.5f - kSpinnerWidth * 0.5f);
        activityIndicatorFrame.origin.y=activityIndicatorFrame.origin.y+(kActivtyProgressViewWidth*0.5f);
        _activityIndicatorView.frame = activityIndicatorFrame;
        
        CGRect activityLabelFrame = _activityLabel.frame;
        activityLabelFrame.origin.x=activityLabelFrame.origin.y+(kActivtyProgressViewWidth*0.5f);
        activityLabelFrame.origin.y = (CGRectGetHeight(_activityIndicatorView.frame) + 5)+(kActivtyProgressViewWidth*0.5f);
        _activityLabel.frame = activityLabelFrame;
        
    }
    else
    {
        activityProgressView.frame = CGRectMake(self.frame.size.width * 0.5f - kSpinnerWidth * 0.5f, self.frame.size.height * 0.5f - kSpinnerHeight * 0.5f, kSpinnerWidth, kSpinnerHeight);
    }
}
/**
 Method startAnimating
 @param: progressText send NSString as parameter.
 given text is showing with indicator
 */
- (void)startAnimating:(NSString *)progressText
{
    _isAnimating = YES;
    self.hidden = NO;
    [self alignProgressIndicator:progressText];
    [_activityIndicatorView startAnimating];
}

/**
 Method stopAnimating
 it will stop activityIndicatorView animating
 */
- (void)stopAnimating
{
    _isAnimating = NO;
    self.hidden = YES;
    [_activityIndicatorView stopAnimating];
}


@end
