//
//  SplNeedsController.m
//  P2PDinner
//
//  Created by Selvam M on 4/23/15.
//  Copyright (c) 2015 P2PDinner. All rights reserved.
//

#import "SplNeedsController.h"
#import "StringConstants.h"

@implementation SplNeedsController
@synthesize itemDetails;
- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section
{
    UIView *view = [[UIView alloc] initWithFrame:CGRectMake(0, 0, tableView.frame.size.width, 58)];
    /* Create custom view to display section header... */
    UILabel *label = [[UILabel alloc] initWithFrame:CGRectMake(20, 30, tableView.frame.size.width, 18)];
    [label setTextColor:[UIColor colorWithRed:237.0/255.0 green:134.0/255.0 blue:0.0/255.0 alpha:1]];
    [label setFont:[UIFont fontWithName:kFont_Name size:18]];
    NSString *string =kSpecial_Needs;
    /* Section header is in 0th index... */
    [label setText:string];
    [view addSubview:label];
    [view setBackgroundColor:[UIColor clearColor]]; //your background color...
    return view;
}
- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section{
    return 58.0f;
}
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    
    return 1;
}
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)
indexPath
{
    static NSString *simpleTableIdentifier= kSplNeedsCell;
    specialNeedsCell *splNeedsCell;
    splNeedsCell=(specialNeedsCell *)[tableView dequeueReusableCellWithIdentifier:simpleTableIdentifier];
    veganSwitch=(UISwitch *)[splNeedsCell viewWithTag:11];
    kosherSwitch=(UISwitch *)[splNeedsCell viewWithTag:44];
    vegetarianSwitch=(UISwitch *)[splNeedsCell viewWithTag:33];
    glutenFreeSwitch=(UISwitch *)[splNeedsCell viewWithTag:55];
    halalSwitch=(UISwitch *)[splNeedsCell viewWithTag:22];
    diabetic=(UISwitch *)[splNeedsCell viewWithTag:66];
    lowCarb=(UISwitch *)[splNeedsCell viewWithTag:77];
    lowFatDiet=(UISwitch *)[splNeedsCell viewWithTag:88];
    [veganSwitch setOn:[self swichActionForString:itemDetails.dinnerSpecialNeeds withAction:kVegan]];
    [kosherSwitch setOn:[self swichActionForString:itemDetails.dinnerSpecialNeeds withAction:kKosher]];
    [vegetarianSwitch setOn:[self swichActionForString:itemDetails.dinnerSpecialNeeds withAction:kVegetarian]];
    [glutenFreeSwitch setOn:[self swichActionForString:itemDetails.dinnerSpecialNeeds withAction:kGluten_Free]];
    [halalSwitch setOn:[self swichActionForString:itemDetails.dinnerSpecialNeeds withAction:kHalal]];
    [diabetic setOn:[self swichActionForString:itemDetails.dinnerSpecialNeeds withAction:kDiabetic]];
    [lowCarb setOn:[self swichActionForString:itemDetails.dinnerSpecialNeeds withAction:kLow_Carb]];
    [lowFatDiet setOn:[self swichActionForString:itemDetails.dinnerSpecialNeeds withAction:kLow_Fat_Diet]];
    
    
    splNeedsCell.selectionStyle =UITableViewCellSelectionStyleNone;
    return splNeedsCell;
    
}
- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    return 489;
}
#pragma args switchAction
- (BOOL)swichActionForString:(NSString *)stringValue withAction:(NSString *)value{
    stringValue=[stringValue uppercaseString];
    value=[value uppercaseString];
    
    if ([stringValue rangeOfString:value].location == NSNotFound) {
            return NO;
    }else{
            return YES;
    }
    
    return NO;
}
- (NSString *)getSplNeedsString{
    NSMutableArray *splNeedsStrinArray=[[NSMutableArray alloc]init];
    
    if (veganSwitch.isOn) {
        [splNeedsStrinArray addObject:kVegan];
    }
    
    if (kosherSwitch.isOn) {
        [splNeedsStrinArray addObject:kKosher];
    }
    
    if (vegetarianSwitch.isOn) {
        [splNeedsStrinArray addObject:kVegetarian];
    }
    
    if (glutenFreeSwitch.isOn) {
        [splNeedsStrinArray addObject:kGluten_Free];
    }
    
    if (halalSwitch.isOn) {
        [splNeedsStrinArray addObject:kHalal];
    }
    
    if (diabetic.isOn) {
        [splNeedsStrinArray addObject:kDiabetic];
    }
    
    if (lowCarb.isOn) {
        [splNeedsStrinArray addObject:kLow_Carb];
    }
    
    if (lowFatDiet.isOn) {
        [splNeedsStrinArray addObject:kLow_Fat_Diet];
    }
    
    return [splNeedsStrinArray componentsJoinedByString:@","];
}
-(IBAction)updateItem:(id)sender{
    itemDetails.dinnerSpecialNeeds=[self getSplNeedsString];
}

@end
