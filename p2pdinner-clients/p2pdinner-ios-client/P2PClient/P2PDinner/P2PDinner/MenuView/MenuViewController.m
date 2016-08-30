//
//  MenuViewController.m
//  P2PDinner
//
//  Created by Selvam M on 4/16/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import "MenuViewController.h"
#import "PageSnapView.h"
#import "Utility.h"
#import "CreateFoodItem.h"
#import "AddFoodPhotos.h"
#import "AddTimeController.h"
#import "CostViewController.h"
#import "PlaceViewController.h"
#import "SplNeedsController.h"
#import "MyOrderViewController.h"
#import "MyOrderItem.h"
#import "AppDelegate.h"
#import "HomeViewController.h"

@interface MenuViewController()<PageSnapViewDelegate>
{
    NSArray *pageArray;
    PageSnapView *pageView;
    //PageSnapViewViewControllers
    CreateFoodItem *createFoodItem;
    AddFoodPhotos *addFoodPhotos;
    AddTimeController *addTimeController;
    
    CostViewController *costViewController;
    PlaceViewController *placeViewController;
    SplNeedsController *splNeedsController;
    
}
-(void)createDinnerViewControllerAction;
-(void)addFoodPhotosViewControllerAction;
-(void)addTimeViewControllerAction;
@end

@implementation MenuViewController
@synthesize itemDetails;
-(void)setUpMenu{
    pageArray=@[@"food",@"photo",@"time",@"place",@"cost",@"splNeeds"];
    pageView = [[PageSnapView alloc] initWithFrame:CGRectMake(0, CGRectGetHeight(self.view.frame)-48,  CGRectGetWidth(self.view.frame), 56) andData:pageArray];
    [pageView setBackgroundColor:[UIColor colorWithRed:224.0/255.0 green:224.0/255.0 blue:224.0/255.0 alpha:1]];
    pageView.isAccessibilityElement=YES;
    pageView.pageDelegate = self;
    [self.view addSubview:pageView];
}
- (void)cancelAction{
        [self updateMenuItem:[[ItemDetailsShared sharedItemDetails] sharedItemDetailsValue]];
    [self dismissViewControllerAnimated:YES completion:nil];
}
- (void)naviagtionBarUISetup{
    UIColor *navBarColor=[UIColor colorWithRed:237.0/255.0 green:134.0/255.0 blue:0.0/255.0 alpha:1];
    NSDictionary *navbarTitleTextAttributes = [NSDictionary dictionaryWithObjectsAndKeys:
                                               [UIColor whiteColor],UITextAttributeTextColor,
                                               [UIFont fontWithName:@"Plantin" size:24], NSFontAttributeName,[NSValue valueWithUIOffset:UIOffsetMake(-1, 0)],UITextAttributeTextShadowOffset, nil];
    [self.navigationController.navigationBar  setTitleTextAttributes:navbarTitleTextAttributes];
    [self.navigationController.navigationBar setBarTintColor:navBarColor];
    [self.navigationItem.leftBarButtonItem setTintColor:[UIColor whiteColor]];
}
- (void)setUpNavigationBar{
    [self naviagtionBarUISetup];
    UIButton *button = [UIButton buttonWithType:UIButtonTypeCustom];
    [button setTitle:@"Cancel" forState:UIControlStateNormal];
    button.frame=CGRectMake(0.0, 100.0, 60.0, 30.0);
    [button addTarget:self action:@selector(cancelAction)  forControlEvents:UIControlEventTouchUpInside];
    self.navigationItem.leftBarButtonItem = [[UIBarButtonItem alloc] initWithCustomView:button];
    
}
- (void)viewDidLoad {
    [super viewDidLoad];
    self.title=@"Dinner Listing";
    if (!itemDetails) {
        itemDetails=[[ItemDetails alloc]init];
    }
    
    [self.navigationItem setHidesBackButton:YES];
    if ([Utility isIOS9]) {
        [self setUpNavigationBar];
    }
    [self setUpMenu];
    nextButton.layer.cornerRadius = 5;
    [self createDinnerViewControllerActionFirstTime];
    
}
- (void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    [nextButton setUserInteractionEnabled:YES];
    
}
- (IBAction)nextButtonAction:(id)sender{
    
    if ([nextButton.titleLabel.text isEqualToString:@"Sell"]) {
        [nextButton setUserInteractionEnabled:NO];
        NSLog(@"need to call add to item listing");
        [self updateMenuItem:[[ItemDetailsShared sharedItemDetails] sharedItemDetailsValue] withUpdateCallBack:^{
            [self addToDinnerListing:[[ItemDetailsShared sharedItemDetails] sharedItemDetailsValue]];
        }];
        
    }
    else if([nextButton.titleLabel.text isEqualToString:@"Add to cart"])
    {
        NSLog(@"Need to implement cart adding action");
    }
    else{
        [self updateMenuItem:[[ItemDetailsShared sharedItemDetails] sharedItemDetailsValue]];
        [pageView setNextSelectedIndex];
    }
    
}
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender{
    if ([segue.identifier isEqualToString:@"MyOrderViewController"]) {
        
    }
}
#pragma mark PageSnapView Delegate
- (void)selectedIndexWithIndex:(NSInteger)index{
    
}
- (void)showImageWithIndex:(NSInteger)index andFrame:(CGRect)pageRect{
    
}
- (void)showImageWithIndex:(NSInteger)index andFrame:(CGRect)pageRect image:(UIImage *)img{
    
}
- (void)updateLabel:(NSInteger)index{
    
    [nextButton setUserInteractionEnabled:YES];
    if(([pageArray count]-1)==index){
             [nextButton setTitle:@"Sell" forState:UIControlStateNormal];
    }else
    {
        [nextButton setTitle:@"Next" forState:UIControlStateNormal];
    }
    
    switch (index) {
        case 0:
        {
            [self removeViewFromDisplayView];
            [self createDinnerViewControllerAction];
            
        }
            break;
            
        case 1:
        {
            
            [self removeViewFromDisplayView];
            [self addFoodPhotosViewControllerAction];
            
        }
            break;
        case 2:
        {
            [self removeViewFromDisplayView];
            [self addTimeViewControllerAction];
            
        }
            break;
        case 3:
        {
            [self removeViewFromDisplayView];
            [self placeViewControllerAction];
            
        }
            break;
        case 4:
        {
            [self removeViewFromDisplayView];
            [self costViewControllerAction];
            
        }
            break;
        case 5:
        {
            [self removeViewFromDisplayView];
            [self splNeedsControllerAction];
            
        }
            break;
            
            
            
        default:
            break;
    }
    
    
}
#pragma mark SetupPage
- (void)removeViewFromDisplayView{
    UIView *view=[_displayView viewWithTag:1000];
    if (view) {
        [view removeFromSuperview];
        [addFoodPhotos removeFromParentViewController];
        [addTimeController removeFromParentViewController];
        [createFoodItem removeFromParentViewController];
        [costViewController removeFromParentViewController];
        [placeViewController removeFromParentViewController];
        [splNeedsController removeFromParentViewController];
    }
    
}
- (UIStoryboard *)getStoryBoard{
    return [UIStoryboard storyboardWithName:@"Main" bundle:[NSBundle mainBundle]];
}
- (void)createDinnerViewControllerActionFirstTime{
      CreateFoodItem  *createFoodItem1 = (CreateFoodItem *)[[self getStoryBoard] instantiateViewControllerWithIdentifier:@"FoodItemViewController"];
        [createFoodItem1 setItemDetails:[[ItemDetailsShared sharedItemDetails] sharedItemDetailsValue]];
        createFoodItem1.view.tag=1000;
    CGRect viewControllerFrame=createFoodItem1.view.frame;
        _displayView.frame = viewControllerFrame;
    [createFoodItem1.createFoodTable setContentInset:UIEdgeInsetsMake(-64,0, 0, 0)];
       // [createFoodItem1.createFoodTable reloadData];
     [self addChildViewController:createFoodItem1];
    [_displayView addSubview:createFoodItem1.view];
}
- (void)createDinnerViewControllerAction{
    if (!createFoodItem) {
        createFoodItem = (CreateFoodItem *)[[self getStoryBoard] instantiateViewControllerWithIdentifier:@"FoodItemViewController"];
        [createFoodItem setItemDetails:[[ItemDetailsShared sharedItemDetails] sharedItemDetailsValue]];
        createFoodItem.view.tag=1000;
        _displayView.frame = createFoodItem.view.frame;
        [createFoodItem.createFoodTable reloadData];
        
    }
    [self addChildViewController:createFoodItem];
    [_displayView addSubview:createFoodItem.view];
}
- (void)addFoodPhotosViewControllerAction{
    if (!addFoodPhotos) {
        addFoodPhotos = (AddFoodPhotos *)[[self getStoryBoard] instantiateViewControllerWithIdentifier:@"addFoodPhotosViewController"];
        [addFoodPhotos setItemDetails:[[ItemDetailsShared sharedItemDetails] sharedItemDetailsValue]];
        addFoodPhotos.view.tag=1000;
        
    }
    _displayView.frame = addFoodPhotos.view.frame;
    [self addChildViewController:addFoodPhotos];
    
    [_displayView addSubview:addFoodPhotos.view];
}
- (void)addTimeViewControllerAction{
    if (!addTimeController) {
        addTimeController = (AddTimeController *)[[self getStoryBoard] instantiateViewControllerWithIdentifier:@"addTimeViewController"];
        [addTimeController setItemDetails:[[ItemDetailsShared sharedItemDetails] sharedItemDetailsValue]];
        addTimeController.view.tag=1000;
    }
    _displayView.frame = addTimeController.view.frame;
    [self addChildViewController:addTimeController];
    [_displayView addSubview:addTimeController.view];
}

- (void)placeViewControllerAction{
    if (!placeViewController) {
        placeViewController = (PlaceViewController *)[[self getStoryBoard] instantiateViewControllerWithIdentifier:@"PlaceViewController"];
        [placeViewController setItemDetails:[[ItemDetailsShared sharedItemDetails] sharedItemDetailsValue]];
        placeViewController.view.tag=1000;
    }
    _displayView.frame = placeViewController.view.frame;
    [self addChildViewController:placeViewController];
    [_displayView addSubview:placeViewController.view];
}
- (void)costViewControllerAction{
    if (!costViewController) {
        costViewController = (CostViewController *)[[self getStoryBoard] instantiateViewControllerWithIdentifier:@"CostViewController"];
        [costViewController setItemDetails:[[ItemDetailsShared sharedItemDetails] sharedItemDetailsValue]];
        costViewController.view.tag=1000;
    }
    _displayView.frame = costViewController.view.frame;
    [self addChildViewController:costViewController];
    [_displayView addSubview:costViewController.view];
}
- (void)splNeedsControllerAction{
    if (!splNeedsController) {
        splNeedsController = (SplNeedsController *)[[self getStoryBoard] instantiateViewControllerWithIdentifier:@"SplNeedsController"];
        [splNeedsController setItemDetails:[[ItemDetailsShared sharedItemDetails] sharedItemDetailsValue]];
        splNeedsController.view.tag=1000;
    }
    _displayView.frame = splNeedsController.view.frame;
    [self addChildViewController:splNeedsController];
    [_displayView addSubview:splNeedsController.view];
    
}
-(NSNumber *)getDinnerId:(ItemDetails *)itemDetail{
    return itemDetail.dinnerId;
}
- (void)updateMenuItem:(ItemDetails *)itemDetails1 withUpdateCallBack:(updateCallback)callBack{
    [[SellerHistoryHandler sharedSellerHistoryHandler]updateMenuItem:itemDetails1 serviceCallBack:^(NSError *error, ItemDetails *response) {
        if (!error) {
            self.itemDetails=response;
            [[[ItemDetailsShared sharedItemDetails] sharedItemDetailsValue] setDinnerId:[self getDinnerId:response]];
            [itemDetails setDinnerId:[self getDinnerId:response]];
        }else{
            if (error.code==421) {
                [[[UIAlertView alloc]initWithTitle:@"Title Error" message:@"Please Enter valid dinnertitle" delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil]show];
                
            }else if (error.code==422) {
                [[[UIAlertView alloc]initWithTitle:@"Category Error" message:@"Please Enter valid Category" delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil]show];
                
            }else if (error.code==423) {
                [[[UIAlertView alloc]initWithTitle:@"Address Error" message:@"Please Enter valid Address" delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil]show];
                
            }
        }
        callBack();
    }];
}
- (void)updateMenuItem:(ItemDetails *)itemDetails1{
    
    [[SellerHistoryHandler sharedSellerHistoryHandler]updateMenuItem:itemDetails1 serviceCallBack:^(NSError *error, ItemDetails *response) {
        if (!error) {
            self.itemDetails=response;
            [[[ItemDetailsShared sharedItemDetails] sharedItemDetailsValue] setDinnerId:[self getDinnerId:response]];
            [itemDetails setDinnerId:[self getDinnerId:response]];
        }else{
            if (error.code==421) {
                [[[UIAlertView alloc]initWithTitle:@"Title Error" message:@"Please Enter valid dinnertitle" delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil]show];
                
            }else if (error.code==422) {
                [[[UIAlertView alloc]initWithTitle:@"Category Error" message:@"Please Enter valid Category" delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil]show];
                
            }else if (error.code==423) {
                [[[UIAlertView alloc]initWithTitle:@"Address Error" message:@"Please Enter valid Address" delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil]show];
                
            }
        }
    }];
}
- (void)addToDinnerListing:(ItemDetails *)itemDetails1{
    [[SellerHistoryHandler sharedSellerHistoryHandler]addDinnerListingMenuItem:itemDetails1 serviceCallBack:^(NSError *error, AddDinnerListItem *response) {
        if (!error) {
            
            NSLog(@"startTime:%@ endTime%@ closeTime%@",response.startTime,response.endTime,response.closeTime);
            [self dismissViewControllerAnimated:NO completion:^{
                AppDelegate *appDelegate = (AppDelegate *)[[UIApplication sharedApplication] delegate];
                UIWindow *mainWindow = appDelegate.window;
                UINavigationController *myNavCon = (UINavigationController*)mainWindow.rootViewController;
                [myNavCon popToRootViewControllerAnimated:NO];
                HomeViewController *homeViewController=[myNavCon.viewControllers objectAtIndex:0];
                if ([homeViewController respondsToSelector:@selector(moveToMyOrderScreen)]) {
                    [homeViewController moveToMyOrderScreen];
                }
                
            }];
           // [self performSegueWithIdentifier:@"MyOrderViewController" sender:self];
        }
        else{
            if (error.code==420) {
                [[[UIAlertView alloc]initWithTitle:@"Time Error" message:@"Please select valid time" delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil]show];
                [pageView selectPageWithIndex:2];
                [self updateLabel:2];
            }else if (error.code==421) {
                [[[UIAlertView alloc]initWithTitle:@"Title Error" message:@"Please Enter valid dinnertitle" delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil]show];
                [pageView selectPageWithIndex:0];
                [self updateLabel:0];
            }else if (error.code==422) {
                [[[UIAlertView alloc]initWithTitle:@"Category Error" message:@"Please Enter valid Category" delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil]show];
                [pageView selectPageWithIndex:0];
                [self updateLabel:0];
            }else if (error.code==423) {
                [[[UIAlertView alloc]initWithTitle:@"Address Error" message:@"Please Enter valid Address" delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil]show];
                [pageView selectPageWithIndex:3];
                [self updateLabel:3];
            }
        }
    }];
}
- (void)backAction{
    
}
@end
