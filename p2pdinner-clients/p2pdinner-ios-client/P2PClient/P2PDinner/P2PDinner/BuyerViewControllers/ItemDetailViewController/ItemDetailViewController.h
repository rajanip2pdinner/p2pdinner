//
//  ItemDetailViewController.h
//  P2PDinner
//
//  Created by Selvam M on 10/29/15.
//  Copyright © 2015 P2PDinner. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ItemDetails.h"
#import "PagedImageScrollView/PagedImageScrollView.h"

@interface ItemDetailViewController : UITableViewController{
    
}
@property (weak, nonatomic) IBOutlet UIImageView *foodImage;
@property (weak, nonatomic) IBOutlet UIView *foodImageViewCell;
@property (weak, nonatomic) IBOutlet UIImageView *foodImageBg;
@property (weak, nonatomic) IBOutlet UILabel *foodName;
@property (weak, nonatomic) IBOutlet UILabel *foodSellerName;
@property (weak, nonatomic) IBOutlet UILabel *distance;
@property (weak, nonatomic) IBOutlet UILabel *foodAddress;
@property (weak, nonatomic) IBOutlet UILabel *toGoOption;
@property (weak, nonatomic) IBOutlet UILabel *eatInOption;
@property (weak, nonatomic) IBOutlet UILabel *foodPrice;
@property (weak, nonatomic) IBOutlet UILabel *foodAvailableTime;
@property (weak, nonatomic) IBOutlet UILabel *dinnerSelectionCount;
@property (weak, nonatomic) IBOutlet UILabel *totalPrice;
@property (weak, nonatomic) IBOutlet UISegmentedControl *coutSelectionSwitch;
@property(nonatomic,strong)ResultItemDetails *itemDetails;
-(IBAction)segmentAction:(id)sender;
-(IBAction)buyButtonAction:(id)sender;
@end
